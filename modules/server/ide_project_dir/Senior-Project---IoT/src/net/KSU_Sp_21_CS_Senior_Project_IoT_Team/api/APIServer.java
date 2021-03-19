package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class APIServer extends HttpsServer {
    private final Boilerplate BOILER_PLATE;
    private final ExecutorService EXECUTOR;

    public APIServer(ExecutorService executor) {
        this.EXECUTOR = executor;
        this.BOILER_PLATE = new Boilerplate(executor);
    }

    @Override
    public void setHttpsConfigurator(HttpsConfigurator config) {

    }

    @Override
    public HttpsConfigurator getHttpsConfigurator() {
        return null;
    }

    @Override
    public void bind(InetSocketAddress addr, int backlog) throws IOException {

    }

    @Override
    public void start() {

    }

    @Override
    public void setExecutor(Executor executor) {

    }

    @Override
    public Executor getExecutor() {
        return EXECUTOR;
    }

    @Override
    public void stop(int delay) {
        BOILER_PLATE.shutdown();
    }

    @Override
    public HttpContext createContext(String path, HttpHandler handler) {
        return null;
    }

    @Override
    public HttpContext createContext(String path) {
        return null;
    }

    @Override
    public void removeContext(String path) throws IllegalArgumentException {

    }

    @Override
    public void removeContext(HttpContext context) {

    }

    @Override
    public InetSocketAddress getAddress() {
        return null;
    }

    public static void main(String[] args) {
        final ThreadPoolExecutor executorPool = null; // TODO: set thread pool params
        final APIServer server = new APIServer(executorPool);
        server.start();
    }
}
