package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.data_routes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.APIHandler;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao.DeviceDao;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao.ScheduleCostDao;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao.ScheduleDao;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Device;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Schedule;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public DeviceHandler() {
        super(MATCHER);

        deviceDao = new DeviceDao();
        scheduleDao = new ScheduleDao();
        costDao = new ScheduleCostDao();
    }

    // TODO: implement
    @Override
    public void doGET(HttpExchange exchange) {
        //START for Josh Yang
        List<String> uriParts = Utils.getPathParts(exchange.getRequestURI());

        String response = null;
        switch (uriParts.size()) {
            // /devices
            case 2 -> {

            }
            // /devices/{serial}
            case 3 -> {
                //changed getDeviceBySerial() to public so I don't have to use secureGetDeviceBySerial().
                //have to change getDeviceBySerial() to secureGetDeviceBySerial().
                Device device = deviceDao.getDeviceBySerial(uriParts.get(2));

                if (device == null) {
                    try {
                        Utils.sendInternalServerError(exchange);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    return;
                }

                response = gson.toJson(device, Device.class);
            }
            // /devices/{serial}/schedules
            // getScedules() is a dummy function for secureGetSchedules()
            case 4 -> {
                List<Schedule> schedule = scheduleDao.getSchedules(uriParts.get(2), true);
                if (schedule == null) {
                    try {
                        Utils.sendInternalServerError(exchange);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    return;
                }

                response = gson.toJson(schedule);
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
        }
        //END for Josh Yang

    // START Example by Andrew
//        String response = null;
//        // TODO: use the query map to narrow the search
//        switch (uriParts.size()) { // TODO: try out lambda style
//            // root resource
//            // /devices: returns devices owned by a user
//            case 2 -> {
//                Map<String, String> queryMap = Utils.parseQueryString(exchange.getRequestURI().getQuery());
//                response = gson.toJson(APIModel.getDefault(Device.class), Device.class); // TODO: remove this default value
//            }
//            // get particular device
//            // /devices/{serial}: returns attributes of the given device
//            case 3 -> {
//                Device dfault = APIModel.getDefault(Device.class);
//                if (dfault == null) {
//                    try {
//                        Utils.sendInternalServerError(exchange);
//                    } catch (IOException ioException) {
//                        ioException.printStackTrace(); // TODO: proper logging
//                    }
//                    return;
//                }
//                Device particularDevice = new Device(
//                        uriParts.get(2),
//                        dfault.accountID,
//                        dfault.ownerID,
//                        dfault.locationID,
//                        dfault.hvacModelID,
//                        dfault.scheduleID
//                );
//                response = gson.toJson(particularDevice, Device.class);
//            }
//            // some attribute of a particular device
//            // /devices/{serial}/schedules or /devices/{serial}/costs
//            case 4 -> response = switch (uriParts.get(3)) {
//                case "schedules" -> gson.toJson(APIModel.getDefault(Schedule.class), Schedule.class);
//                case "costs" -> gson.toJson(APIModel.getDefault(ScheduleCost.class), ScheduleCost.class);
//                default -> response;
//            };
//        }
//        try (PrintWriter out = new PrintWriter(exchange.getResponseBody())) {
//            if (response != null) {
//                exchange.sendResponseHeaders(200, response.length());
//                out.print(response);
//            } else {
//                exchange.sendResponseHeaders(400, -1); // bad request
//            }
//        } catch (IOException ioex) {
//            ioex.printStackTrace(); // TODO: proper logging
//        }
//    }
    // END Example by Andrew

    // TODO: implement
    @Override
    public void doPOST(HttpExchange exchange) {
        //START for Josh Yang
        List<String> uriParts = Utils.getPathParts(exchange.getRequestURI());
        String response = null;
        // TODO: actually use the DAOs
        switch (uriParts.size()){
            // /devices/{serial}/schedules
            case 4 -> {
                // getSchedules is a dummy function for secureGetSchedules.
                List<Schedule> schedule = scheduleDao.getSchedules(uriParts.get(3), true);
                // CreateSchedule is a dummy function for secureCreateSchedule().
                scheduleDao.createSchedule(schedule);

                response = gson.toJson(schedule);
            }

        }

        try (PrintWriter out = new PrintWriter(exchange.getResponseBody())) {
            if (response != null) {
                exchange.sendResponseHeaders(200, response.length());
                out.print(response);
            } else {
                exchange.sendResponseHeaders(400, -1);
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
        //END for Josh Yang
    }

    @Override
    public void close() throws IOException {
        deviceDao.close();
        scheduleDao.close();
        costDao.close();
        // TODO
    }
}
