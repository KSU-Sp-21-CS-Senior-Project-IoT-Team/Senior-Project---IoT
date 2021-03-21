package testinghandlers.dao;

import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao.Dao;
import testinghandlers.models.Movie;

import java.io.IOException;

/**
 * This is the internal API class that manages Movie objects. It is at the layer of other API logic, and is exposed
 * to APIHandlers to bridge from REST API layer to API logic layer.
 */
public class Movies implements Dao {
    private final Movie testingMovie = new Movie("Cool New Movie", "A brand new original movie", "Action", 5.0, 1234);

    public Movie get(long id) {
        /*
         * You'll want to get the info from the database here, and parse the results into a Movie object.
         * Later on, a caching scheme can be used to here to minimize unnecessary database queries and speed things up.
         */
        if (id == 1234) {
            return testingMovie;
        }

        // null indicates no such record
        return null;
    }

    @Override
    public void close() throws IOException {
        /*
         * Here, you will close your data resources like database connections, file io, etc.
         */
        System.out.println("Movies DAO closed!");
    }
}
