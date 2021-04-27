package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.data_routes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.APIHandler;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao.DeviceDao;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao.ForecastDao;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao.UserDao;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Device;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.User;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public class UserHandler extends APIHandler {
    static {
        /*
         * This line is the only one you really care about. Define a regular expression that
         * describes all of the API paths that this handler will service. For regex help,
         * see https://www.regexr.com.
         *
         * Otherwise, just make sure the bottom few lines have the name of this class.
         */
        final Pattern pattern = PATTERN = Pattern.compile("/api/users(/[\\da-zA-Z0-9\\-_]+(/devices)?)?");

        final Function<String, Boolean> matcher = MATCHER = s -> pattern.matcher(s).matches();
        APIHandler.CHILD_MATCHER_MAP.put(UserHandler.class, matcher);
        APIHandler.CHILD_CONS_MAP.put(UserHandler.class, UserHandler::new);
    }
    private static final Pattern PATTERN;
    private static final Function<String, Boolean> MATCHER;

    private final UserDao userDao;
    private final DeviceDao deviceDao;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public UserHandler() {
        super(MATCHER);

        userDao = new UserDao();
        deviceDao = new DeviceDao();
    }

    // TODO: implement
    @Override
    public void doGET(HttpExchange exchange) {
        //START for Josh Yang
        List<String> uriParts = Utils.getPathParts(exchange.getRequestURI());

        String response = null;

        switch (uriParts.size()){
            // /users/{user_id}/devices
            case 4 -> {
                Device device = deviceDao.getDevicesByUserID(uriParts.get(2));

                if (device == null) {
                    try {
                        Utils.sendInternalServerError(exchange);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    return;
                }
                System.out.println(response);
                response = gson.toJson(device);
            }
        }

        try (PrintWriter out = new PrintWriter(exchange.getResponseBody())) {
            if (response != null) {
                exchange.sendResponseHeaders(200, response.length());
                out.print(response);
            } else {
                exchange.sendResponseHeaders(400, -1); // bad request
            }
        } catch (IOException ioex) {
            ioex.printStackTrace(); // TODO: proper logging
        }
        //END for Josh Yang
    }

    // TODO: implement
    @Override
    public void doPOST(HttpExchange exchange) {

    }

    @Override
    public void close() throws IOException {
        userDao.close();
        deviceDao.close();
        // TODO
    }
}
