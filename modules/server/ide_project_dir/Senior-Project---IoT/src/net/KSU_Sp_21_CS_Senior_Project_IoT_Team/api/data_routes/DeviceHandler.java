package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.data_routes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.APIHandler;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.models.Token;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao.DeviceDao;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao.ScheduleCostDao;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao.ScheduleDao;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Device;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Schedule;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.SimpleSchedule;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
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
        List<String> uriParts = Utils.getPathParts(exchange.getRequestURI());

        String response = null;
        switch (uriParts.size()) {
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
            case 4 -> {
                SimpleSchedule schedule = scheduleDao.getScheduleByDevice(uriParts.get(2));
                System.out.println(schedule);


                response = gson.toJson(schedule, SimpleSchedule.class);
				if (schedule == null) {
                    try {
                        Utils.sendInternalServerError(exchange);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    return;
                }
            }
        }

            try (PrintWriter out = new PrintWriter(exchange.getResponseBody())) {
                if (response != null) {
                    exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
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
        int status = 400;
        switch (uriParts.size()){
            // /devices/{serial}
            case 3 -> {
                Device device = deviceDao.getDeviceBySerial(uriParts.get(2));

                ScheduleInput scheduleInput = null;
                try (InputStreamReader inputByUser = new InputStreamReader(exchange.getRequestBody())) {
                    scheduleInput = gson.fromJson(inputByUser, (Type) ScheduleInput.class);
                } catch (IOException ioex) {
                    ioex.printStackTrace();
                }

                // token ignored
                Token token = null;
                deviceDao.secureSetAttributes(
                        uriParts.get(2),
                        scheduleInput.schedule,
                        scheduleInput.location,
                        scheduleInput.model,
                        token
                );

                response = gson.toJson(device, Device.class);
            }
            // /devices/{serial}/schedules
            case 4 -> {
                Schedule schedule = null;
                // getSchedules is a dummy function for secureGetSchedules.
                try (InputStreamReader inputByUser = new InputStreamReader(exchange.getRequestBody())) {
                    schedule = gson.fromJson(inputByUser, Schedule.class);
                } catch (IOException ioex) {
                    ioex.printStackTrace();
                }

                // CreateSchedule is a dummy function for secureCreateSchedule().
                scheduleDao.createSchedule(schedule);

                status = 200;
            }

        }

        try (PrintWriter out = new PrintWriter(exchange.getResponseBody())) {
            if (response != null) {
                exchange.getResponseHeaders().set("Content-type", "application/json");
                exchange.sendResponseHeaders(200, response.length());
                out.print(response);
            } else {
                exchange.sendResponseHeaders(status, -1);
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
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
