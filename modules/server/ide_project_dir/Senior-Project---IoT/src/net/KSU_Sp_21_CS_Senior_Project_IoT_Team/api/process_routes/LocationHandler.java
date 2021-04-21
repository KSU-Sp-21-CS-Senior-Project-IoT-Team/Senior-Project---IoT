package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.process_routes;

import com.sun.net.httpserver.HttpExchange;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.APIHandler;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao.ForecastDao;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao.LocationDao;

import java.io.IOException;
import java.util.function.Function;
import java.util.regex.Pattern;

public class LocationHandler extends APIHandler {
    static {
        /*
         * This line is the only one you really care about. Define a regular expression that
         * describes all of the API paths that this handler will service. For regex help,
         * see https://www.regexr.com.
         *
         * Otherwise, just make sure the bottom few lines have the name of this class.
         */
        final Pattern pattern = PATTERN = Pattern.compile("/api/locations(/[\\da-f]+(/forecasts)?)?");

        final Function<String, Boolean> matcher = MATCHER = s -> pattern.matcher(s).matches();
        APIHandler.CHILD_MATCHER_MAP.put(LocationHandler.class, matcher);
        APIHandler.CHILD_CONS_MAP.put(LocationHandler.class, LocationHandler::new);
    }
    private static final Pattern PATTERN;
    private static final Function<String, Boolean> MATCHER;

    private final LocationDao locationDao;
    private final ForecastDao forecastDao;

    public LocationHandler() {
        super(MATCHER);

        locationDao = new LocationDao();
        forecastDao = new ForecastDao();
    }

    // TODO: implement
    @Override
    public void doGET(HttpExchange exchange) {
        // TODO: Josh
    }

    @Override
    public void close() throws IOException {
        locationDao.close();
        forecastDao.close();
        // TODO
    }
}
