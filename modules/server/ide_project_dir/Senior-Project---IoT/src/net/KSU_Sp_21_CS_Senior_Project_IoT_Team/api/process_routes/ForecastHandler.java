package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.process_routes;

import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.APIHandler;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao.ForecastDao;

import java.io.IOException;
import java.util.function.Function;
import java.util.regex.Pattern;

public class ForecastHandler extends APIHandler {
    static {
        /*
         * This line is the only one you really care about. Define a regular expression that
         * describes all of the API paths that this handler will service. For regex help,
         * see https://www.regexr.com.
         *
         * Otherwise, just make sure the bottom few lines have the name of this class.
         */
        final Pattern pattern = PATTERN = Pattern.compile(""); // TODO: regex for api path

        final Function<String, Boolean> matcher = MATCHER = s -> pattern.matcher(s).matches();
        APIHandler.CHILD_MATCHER_MAP.put(ForecastHandler.class, matcher);
        APIHandler.CHILD_CONS_MAP.put(ForecastHandler.class, ForecastHandler::new);
    }
    private static final Pattern PATTERN;
    private static final Function<String, Boolean> MATCHER;

    private final ForecastDao dao;

    public ForecastHandler() {
        super(MATCHER);

        dao = new ForecastDao();
    }

    @Override
    public void close() throws IOException {
        dao.close();
        // TODO
    }
}
