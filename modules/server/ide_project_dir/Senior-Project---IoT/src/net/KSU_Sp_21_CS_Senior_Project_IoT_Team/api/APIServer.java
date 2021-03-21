package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Factory;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Utils;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Wrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * All HTTP connections go through this boilerplate. This is a critical component of the API.
 * This boilerplate implementation can register an arbitrary number of handlers, and they are instantiated
 * as-needed. If a handler has not serviced a request in a specified duration of time, it will be closed and
 * deleted by GC.
 * This boilerplate can, in a future update, support multiple instances of handlers according to traffic needs.
 *
 * This boilerplate assigns tasks to worker threads, which are created at init time according to the available
 * resources of the system. These threads do not go away until the server is shut down*, and are assigned tasks via a
 * priority queue.
 */
public class APIServer implements HttpHandler {
    // the actual url-service mapping & executors
    public final Map<Function<String, Boolean>, Factory<? extends APIHandler>> ROUTING_MAP = new HashMap<>(); // TODO: private
    public final Map<Factory<? extends APIHandler>, PriorityQueue<APIHandler>> INSTANCE_MAP = new HashMap<>(); // TODO: private
    private final ExecutorService EXECUTOR; // worker threads to service http connections

    // synchronization & shutdown
    private volatile boolean alive = true; // used to safely shutdown the server
    private final Wrapper<Integer> callCount = new Wrapper<>(0); // acts as an "inverted" semaphore
    private final Object shutdownSleepObj = new Object(); // used for shutdown timing

    public APIServer(ExecutorService executor) {
        this.EXECUTOR = executor;
    }

    /**
     * A streamlined method to register handlers. Exploits static child class registration with base class.
     * If you have an APIHandler child class you want to register, assuming it has statically
     * registered itself with the base class, you simply call like so:
     * <code>apiServer.registerHandlerClass(MyAPIHandlerClassName.class);</code>
     * and that's it.
     * @param clazz
     * @param <C>
     * @return
     */
    public <C extends APIHandler> boolean registerHandlerClass(Class<C> clazz) {
        return addHandler(APIHandler.factory(clazz));
    }

    /**
     * Registers a handler factory for its path.
     * @param handlerFactory
     * @return
     */
    public boolean addHandler(Factory<? extends APIHandler> handlerFactory) {
        synchronized (callCount) {
            if (!alive) return false;
            callCount.val++;
        }
        //System.out.println(handlerFactory.matcher());
        try {
            synchronized (ROUTING_MAP) {
                synchronized (INSTANCE_MAP) {
                    if (!ROUTING_MAP.containsKey(handlerFactory.matcher())) {
                        ROUTING_MAP.put(
                                handlerFactory.matcher(),
                                handlerFactory
                        );
                        INSTANCE_MAP.put(
                                handlerFactory,
                                new PriorityQueue<>(
                                        (o1, o2) -> o2.load - o1.load // this will order them s.t. lowest load will be at front of queue.
                                )
                        );
                        return true;
                    } else return false;
                }
            }
        } finally {
            decCallCount();
        }
    }

    /**
     * This method routes an exchange to a registered handler, if there is one.
     * If there is no registered handler for the path of this request, error 404 is returned.
     * This can, in future versions, optimize path pattern matching by using a priority queue of patterns based on
     * the number of matches a given pattern has made.
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        synchronized (callCount) {
            if (!alive) return;
            callCount.val++;
        }

        try {
            // extract path & method, call corresponding method from appropriate handler. init a new handler if need be.
            final String path = exchange.getRequestURI().getRawPath();
            final SupportedHTTPMethod method = SupportedHTTPMethod.fromString(exchange.getRequestMethod());
            final APIHandler handler = getHandler(path);
            Consumer<HttpExchange> processor = null;
            if (handler != null) {
                switch (SupportedHTTPMethod.fromString(exchange.getRequestMethod())) {
                    case GET -> processor = handler::doGET;
                    case POST -> processor = handler::doPOST;
                    case PUT -> processor = handler::doPUT;
                    case DELETE -> processor = handler::doDelete;
                }
            }
            final Consumer<HttpExchange> finProcessor = processor;
            //System.out.println("processor: " + processor);

            EXECUTOR.execute(() -> {
                if (finProcessor != null) {
                    finProcessor.accept(exchange);
                    //System.out.println("processor accept() finished!");
                }
                else {
                    exchange.close();
                    //System.err.println("Exchange processor was null!");
                }
            });
        } finally {
            decCallCount();
        }
    }

    public APIHandler getHandler(String path) {
        synchronized (callCount) {
            if (!alive) return null;
            callCount.val++;
        }

        try {
            Factory<? extends APIHandler> factory = null;
            APIHandler handler;

            // TODO: This can be replaced by a more efficient search by using a priority queue.
            Set<Function<String, Boolean>> keySet;
            synchronized (ROUTING_MAP) {
                keySet = ROUTING_MAP.keySet();
            }
            //System.out.println("KeySet for matchers: " + keySet.toString());
            for (Function<String, Boolean> matcher : keySet)
                if (matcher.apply(path)) {
                    synchronized (ROUTING_MAP) {
                        factory = ROUTING_MAP.get(matcher);
                    }
                    break;
                }

            if (factory == null) {
                //System.err.println("getHandler(): factory was null!");
                return null;
            }

            synchronized (INSTANCE_MAP) {
                PriorityQueue<APIHandler> instanceQueue = INSTANCE_MAP.get(factory);
                if (instanceQueue.isEmpty())
                    instanceQueue.add(factory.create());
                handler = instanceQueue.peek();
            }

            return handler;
        } finally {
            decCallCount();
        }
    }

    private static void handleBadURIPath(HttpExchange exchange) {
        //TODO: implement, remove testing parts
        try (PrintWriter out = new PrintWriter(exchange.getResponseBody())){
            String response = Utils.getPathParts(exchange.getRequestURI()).toString();
            response += "\n" + exchange.getRequestURI().getRawPath();
            exchange.sendResponseHeaders(400, response.length());
            out.print(response);
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }

    private void decCallCount() {
        synchronized (callCount) {
            callCount.val--;
            synchronized (shutdownSleepObj) {shutdownSleepObj.notify();}
        }
    }

    /**
     * Will block until the server is shut down.
     */
    public synchronized void shutdown() {
        if (!alive) return;
        alive = false; // tells other methods to stop servicing new calls.
        while (true) { // wait for current method calls to terminate.
            synchronized (callCount) {
                if (callCount.val == 0) break;
            }
            synchronized (shutdownSleepObj) {
                try {
                   shutdownSleepObj.wait(1000);
                } catch (InterruptedException iex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        EXECUTOR.shutdown();
        // finished waiting, time to close handlers...
        for (PriorityQueue<? extends APIHandler> handlerQueue : INSTANCE_MAP.values()) {
            for (APIHandler handler : handlerQueue) {
                handler.close();
            }
        }
    }
}
