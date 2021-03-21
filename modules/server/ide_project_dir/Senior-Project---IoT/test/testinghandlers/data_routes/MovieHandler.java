package testinghandlers.data_routes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.APIHandler;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Utils;
import testinghandlers.dao.Movies;
import testinghandlers.models.Movie;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class MovieHandler extends APIHandler {
    /*
     * !! DO THIS FOR ALL APIHandler CHILDREN !!
     * TODO: tidy this up
     */
    static {
        // change the regular expression here as needed
        // handles '/movies', '/movies/movieID', and '/movies/movieID/*'
        final Pattern pattern = Pattern.compile("/api/movies(/[\\da-f]+(/[\\S]*)?)?");
        // you can generally leave this alone, unless you have some special cases you want this to handle that can't be identified with regular expression.
        final Function<String, Boolean> matcher = (input) -> pattern.matcher(input).matches();

        // leave these lines alone; only change the class name to match this class name.
        PATH_PATTERN = pattern;
        MATCHER = matcher;
        APIHandler.CHILD_CONS_MAP.put(MovieHandler.class, /* should match classname */MovieHandler::new);
        APIHandler.CHILD_MATCHER_MAP.put(MovieHandler.class, /* should match classname */MovieHandler.MATCHER);

        Supplier<MovieHandler> supplier = MovieHandler::new;
        System.out.println("handler::new: " + (supplier == null));
        //System.out.println("static matcher: " + MovieHandler.MATCHER.toString()); // TODO: remove testing io
    }

    // make sure you have this two lines in all child classes
    private static final Pattern PATH_PATTERN;
    public static final Function<String, Boolean> MATCHER;

    private static final String PATTERN_GETALL = "";
    private static final String PATTERN_GETBYID = "";
    private static final String PATTERN_SUBSERVICE = "";

    private final Movies dao = new Movies();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public MovieHandler() {
        super(MATCHER);
    }

    @Override
    public void doGET(HttpExchange exchange) {
        super.doGET(exchange);
        List<String> parts = Utils.getPathParts(exchange.getRequestURI());
        parts.remove(parts.get(0));
        int moviesIndex = parts.lastIndexOf("movies");
        Long id = null;
        try {
            id = Long.parseLong(parts.get(moviesIndex + 1));
        } catch (IndexOutOfBoundsException iobex) {
            iobex.printStackTrace();
        } catch (NumberFormatException nfex) {
            nfex.printStackTrace();
        }
        System.out.println("movie id: " + id); // TODO: remove testing io

        if (id != null) {
            Movie movie = dao.get(id);
            try (PrintWriter out = new PrintWriter(exchange.getResponseBody())) {
                final String response = gson.toJson(movie, Movie.class);
                exchange.sendResponseHeaders(200, response.length());
                out.print(response);
                System.out.println("200 response sent!");
            } catch (IOException ioex) {
                ioex.printStackTrace(); // TODO: should use logging, not print statements
            }
        } else { // no data! return error status
            try{
                exchange.sendResponseHeaders(404, -1);
                System.out.println("404 response sent!");
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }
        //exchange.close();
    }

    @Override
    public void close() {
        try {
            dao.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
