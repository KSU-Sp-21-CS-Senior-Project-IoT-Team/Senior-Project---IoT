import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.APIServer;
import testinghandlers.dao.Movies;
import testinghandlers.models.Movie;
import testinghandlers.data_routes.MovieHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MovieTest {
    public static void test1() {
        System.out.println("Starting test 1...");
        System.out.println("Querying for movie 1234");
        Movie movie;
        try(Movies movieDao = new Movies()){ // should always close Dao's! treat them like file readers/writers etc.
            movie = movieDao.get(1234);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(movie, Movie.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void test2(String host, int port) {
        final ThreadPoolExecutor executorPool = new ThreadPoolExecutor(2, 10, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>()); // TODO: set thread pool params
        final APIServer server = new APIServer(executorPool);
        try {
            System.out.println("Creating httpserver and binding API server...");
            final HttpServer httpServer = HttpServer.create(new InetSocketAddress(host, port), 10);
            httpServer.createContext("/api/", server);

            System.out.println("Registering handler classes with API server...");
            server.registerHandlerClass(MovieHandler.class);

            System.out.println("Matcher for movies:");
            System.out.println("\t" + server.ROUTING_MAP.toString());
            System.out.println("\t" + server.INSTANCE_MAP.toString());
            System.out.println("Result of applying movie matcher to correct uri: " + MovieHandler.MATCHER.apply("/api/movies/1234"));

            System.out.println("Handler registered for movie paths: ");
            System.out.println("\t"+ server.getHandler("/api/movies/1234").getClass().getName());

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

    public static void main(String[] args) {
        //test1();
        test2(args[0], Integer.parseInt(args[1]));
    }
}
