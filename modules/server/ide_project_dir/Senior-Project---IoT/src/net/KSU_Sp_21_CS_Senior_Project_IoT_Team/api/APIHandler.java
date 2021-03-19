package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * handles a single API request. Implementing classes MUST be thread-safe,
 * and the relation of all strings to all child-class path patterns that match such a string MUST be an onto relation.
 * Otherwise, the boilerplate COULD become inconsistent.
 */
public abstract class APIHandler implements HttpHandler, Comparable<APIHandler> {
    protected final String PATH_PATTERN;
    protected volatile int load = 0;

    protected APIHandler(String pathPattern) {
        this.PATH_PATTERN = pathPattern;
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

    @Override
    public void handle(HttpExchange exchange) {

    }

    /**
     * This can be used in later versions to support multiple handler instances for efficiency.
     * @param o
     * @return
     */
    @Override
    public int compareTo(APIHandler o) {
        return 0;
    }
}
