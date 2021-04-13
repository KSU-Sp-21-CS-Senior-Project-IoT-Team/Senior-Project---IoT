package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api;

import com.sun.net.httpserver.HttpExchange;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Factory;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * handles a single API request. Implementing classes MUST be thread-safe,
 * and the relation of all strings to all child-class path patterns that match such a string MUST be an onto relation.
 * Otherwise, the boilerplate COULD become inconsistent.
 */
public abstract class APIHandler implements Comparable<APIHandler>, Closeable {
    protected static final Map<Class<? extends APIHandler>, Supplier<? extends APIHandler>> CHILD_CONS_MAP = new HashMap<>();
    protected static final Map<Class<? extends APIHandler>, Function<String, Boolean>> CHILD_MATCHER_MAP = new HashMap<>();

    protected final Function<String, Boolean> MATCHER;

    protected volatile int load = 0;

    protected APIHandler(Function<String, Boolean> matcher) {
        this.MATCHER = matcher;
    }

    public void doGET(HttpExchange exchange) {
        // TODO: add default return of "method not supported"
    }

    public void doPOST(HttpExchange exchange) {
        // TODO: add default return of "method not supported"
    }

    public void doPUT(HttpExchange exchange) {
        // TODO: add default return of "method not supported"
    }

    public void doDelete(HttpExchange exchange) {
        // TODO: add default return of "method not supported"
    }

    protected void sendNotFound(HttpExchange exchange) {
        // TODO: send response for resource not found (code 404)
    }

    public abstract void close() throws IOException;

    /**
     * This can be used in later versions to support multiple handler instances for efficiency.
     * @param o
     * @return
     */
    @Override
    public int compareTo(APIHandler o) {
        return 0;
    }

    public static <C extends APIHandler> Factory<C> factory(Class<C> clazz) {
        try {
            Class.forName(clazz.getName());
        } catch (ClassNotFoundException cnfex) {
            cnfex.printStackTrace();
            return null;
        }
        Supplier<C> factory = null;
        Function<String, Boolean> matcher = null;
        try {
            factory = ((Supplier<C>) CHILD_CONS_MAP.get(clazz));
            matcher = CHILD_MATCHER_MAP.get(clazz);
        } catch (ClassCastException ccex) {
            ccex.printStackTrace();
        }
        if (factory == null || matcher == null) {
            System.err.println("factory or matcher is null!\n\tfactory: " + factory + "\n\tmatcher: " + matcher);
            System.err.println(CHILD_CONS_MAP.toString());
            System.err.println(CHILD_MATCHER_MAP.toString());
            return null;
        }
        final Supplier<C> finFactory = factory;
        final Function<String, Boolean> finMatcher = matcher;
        return new Factory<>() {
            @Override
            public C create() {
                return finFactory.get();
            }

            @Override
            public Function<String, Boolean> matcher() {
                return finMatcher;
            }
        };
    }
}
