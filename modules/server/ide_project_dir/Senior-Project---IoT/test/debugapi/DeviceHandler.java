package debugapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.APIHandler;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.InvalidTokenException;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.models.Token;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao.DeviceDao;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao.ScheduleCostDao;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao.ScheduleDao;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.APIModel;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Device;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Schedule;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.ScheduleCost;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

public class DeviceHandler extends APIHandler {
    static {
        /*
         * This line is the only one you really care about. Define a regular expression that
         * describes all of the API paths that this handler will service. For regex help,
         * see https://www.regexr.com.
         *
         * Otherwise, just make sure the bottom few lines have the name of this class.
         */
        final Pattern pattern = PATTERN = Pattern.compile("/api/devices(/[\\da-f\\-]+(/schedules|/costs)?)?");

        final Function<String, Boolean> matcher = MATCHER = s -> pattern.matcher(s).matches();
        APIHandler.CHILD_MATCHER_MAP.put(DeviceHandler.class, matcher);
        APIHandler.CHILD_CONS_MAP.put(DeviceHandler.class, DeviceHandler::new);
    }
    private static final Pattern PATTERN;
    private static final Function<String, Boolean> MATCHER;

    private final DeviceDao deviceDao;
    private final ScheduleDao scheduleDao;
    private final ScheduleCostDao costDao;

    public DeviceHandler() {
        super(MATCHER);

        deviceDao = new DeviceDao();
        scheduleDao = new ScheduleDao();
        costDao = new ScheduleCostDao();
    }

    // TODO: implement
    @Override
    public void doGET(HttpExchange exchange) {
        final List<String> uriParts = Utils.getPathParts(exchange.getRequestURI());
        /*
        final String authString = exchange.getRequestHeaders().get("Authorization").get(0).split(" ")[1];
        final String[] rawAuthParts = new String(
                Base64.getDecoder().decode(
                        authString
                ),
                StandardCharsets.US_ASCII
        ).split(":");
         */
        //final Token clientToken = new Token(rawAuthParts[1], rawAuthParts[0], -1);
        //System.out.println(gson.toJson(clientToken, Token.class));
        //System.out.println(uriParts.toString());

        //System.out.println(uriParts);
        String response = null;
        // TODO: use the query map to narrow the search
        switch (uriParts.size()) { // TODO: try out lambda style
            // root resource
            case 2 -> {
                try {
                    exchange.sendResponseHeaders(404, -1);
                } catch (IOException ioException) {
                    ioException.printStackTrace(); // TODO: logging
                }
            }
            // get particular device
            case 3 -> {
                try {
                    Map<String, String> queryMap = Utils.parseQueryString(exchange.getRequestURI().getQuery());
                    try {
                        Device device = deviceDao.getDeviceBySerial(uriParts.get(2)/*, clientToken*/);
                        if (device == null) {
                            exchange.sendResponseHeaders(404, -1);
                            return;
                        }
                        response = APIHandler.GSON.toJson(device, Device.class);
                    } catch (Exception e) {
                        exchange.sendResponseHeaders(401, -1);
                        return;
                    }
                } catch (IOException ioex) {
                    ioex.printStackTrace(); // TODO: proper logging
                }
            }
            // some attribute of a particular device
            case 4 -> response = switch (uriParts.get(3)) {
                case "schedules" -> APIHandler.GSON.toJson(APIModel.getDefault(Schedule.class), Schedule.class);
                case "costs" -> APIHandler.GSON.toJson(APIModel.getDefault(ScheduleCost.class), ScheduleCost.class);
                default -> response;
            };
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
    }

    // TODO: implement
    @Override
    public void doPOST(HttpExchange exchange) {
        List<String> uriParts = Utils.getPathParts(exchange.getRequestURI());
        String response = null;
        // TODO: actually use the DAOs
        try {
            exchange.sendResponseHeaders(200, -1);
        } catch (IOException ioex) {
            ioex.printStackTrace(); // TODO: proper logging
        }
    }

    @Override
    public void close() throws IOException {
        deviceDao.close();
        scheduleDao.close();
        costDao.close();
        // TODO
    }
}
