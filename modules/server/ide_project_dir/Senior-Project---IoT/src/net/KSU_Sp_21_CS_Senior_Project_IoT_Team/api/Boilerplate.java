package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Factory;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Wrapper;
import net.mtgsaber.lib.algorithms.Pair;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * All HTTP connections go through this boilerplate. This is a critical component of the API.
 * This boilerplate implementation can register an arbitrary number of handlers, and they are instantiated
 * as-needed. If a handler has not serviced a request in a specified duration of time, it will be closed and
 * deleted by GC.
 * This boilerplate can, in a future update, support multiple instances of handlers according to traffic needs.
 *
 * This boilerplate assigns tasks to worker threads, which are created at init time according to the available
 * resources of the system. These threads do not go away until the server is shut down, and are assigned tasks via a
 * priority queue.
 */
public class Boilerplate implements HttpHandler {
    private final Map<String, Pair<Factory<? extends APIHandler>, PriorityQueue<APIHandler>>> ROUTING_MAP = new HashMap<>();
    private final ExecutorService EXECUTOR;
    private volatile boolean alive = true;
    private final Wrapper<Integer> callCount = new Wrapper<>(0);
    private final Object shutdownSleepObj = new Object();

    public Boilerplate(ExecutorService executor) {
        this.EXECUTOR = executor;
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
        try {
            synchronized (ROUTING_MAP) {
                if (!ROUTING_MAP.containsKey(handlerFactory.pathPattern())) {
                    ROUTING_MAP.put(
                            handlerFactory.pathPattern(),
                            new Pair<>(
                                    handlerFactory,
                                    new PriorityQueue<>(
                                            (o1, o2) -> o2.load - o1.load // this will order them s.t. lowest load will be at front of queue.
                                    )
                            )
                    );
                    return true;
                } else return false;
            }
        } finally {
            synchronized (callCount) {
                callCount.val--;
            }
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
            final String path = exchange.getRequestURI().getPath();
            final SupportedHTTPMethod method = SupportedHTTPMethod.fromString(exchange.getRequestMethod());
            Pair<Factory<? extends APIHandler>, PriorityQueue<APIHandler>> route = null;
            APIHandler handler;

            // TODO: This can be replaced by a more efficient search by using a priority queue.
            Set<String> keySet;
            synchronized (ROUTING_MAP) {
                keySet = ROUTING_MAP.keySet();
            }
            for (String pattern : keySet)
                if (path.matches(pattern)) {
                    synchronized (ROUTING_MAP) {
                        route = ROUTING_MAP.get(pattern);
                    }
                    break;
                }

            if (route == null) return;

            synchronized (route.VAL) {
                if (route.VAL.isEmpty())
                    route.VAL.add(route.KEY.create());
                handler = route.VAL.peek();
            }

            EXECUTOR.submit(() -> {
                if (handler != null)
                    handler.handle(exchange);
            });
        } finally {
            synchronized (callCount) {
                callCount.val--;
                synchronized (shutdownSleepObj) {shutdownSleepObj.notify();}
            }
        }
    }

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
    }
}
