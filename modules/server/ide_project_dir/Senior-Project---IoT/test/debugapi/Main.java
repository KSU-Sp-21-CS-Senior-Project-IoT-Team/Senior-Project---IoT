package debugapi;

import com.sun.net.httpserver.HttpServer;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.APIServer;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.AuthenticationServer;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao.Dao;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.data_routes.DeviceHandler;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.data_routes.UserHandler;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        final String host = args[0];
        final int port = Integer.parseInt(args[1]);
        final ThreadPoolExecutor executorPool = new ThreadPoolExecutor(2, 10, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>()); // TODO: set thread pool params
        final APIServer server = new APIServer(executorPool);
        try {
            System.out.println("Creating httpserver and binding API server...");
            final HttpServer httpServer = HttpServer.create(new InetSocketAddress(host, port), 10);
            httpServer.createContext("/api/", server);

            System.out.println("Initializing API resources...");
            AuthenticationServer.init();
            Dao.setDbConnectionProvider(new File("./config/auth_db_cfg.json"));
            Dao.setSecurity(AuthenticationServer.getAuthProvider());

            System.out.println("Registering handler classes with API server...");

            server.registerHandlerClass(AuthenticationServer.class);
            server.registerHandlerClass(DeviceHandler.class);
            server.registerHandlerClass(UserHandler.class);

            System.out.println("Starting server...");
            httpServer.start();

            System.out.println("Enter any line of text to stop server.");
            Scanner consoleIn = new Scanner(System.in);
            String command = consoleIn.nextLine();
            System.out.println("Shutting down api server...");
            server.shutdown();
            System.out.println("Shutting down http host server...");
            httpServer.stop(5);
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }
}
