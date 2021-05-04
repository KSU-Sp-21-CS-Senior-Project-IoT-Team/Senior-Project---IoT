package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao;

import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.models.Token;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.*;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class ScheduleDao implements Dao {
    private static final double TEMP_TOLERANCE = 0;

    private enum Query {
        SEC_GET_ACTIVE_SCHEDULE(
                "select A.* "
                + "from iot_db.Schedule as A "
                + "join iot_db.Thermostat_Device as B on A.Schedule_ID = B.Active_Schedule "
                + "join iot_db.User as C on B.User_ID = C.User_ID "
                + "where B.Serial_Number = ? and (B.Account_ID = ? or C.Account_ID = ?);"
        ),
        SEC_GET_SCHEDULES(
                "select A.* "
                + "from iot_db.Schedule as A "
                + "join iot_db.Thermostat_Device as B on A.Device_Serial = B.Serial_Number "
                + "join iot_db.User as C on B.User_ID = C.User_ID "
                + "where B.Serial_Number = ? and (B.Account_ID = ? or C.Account_ID = ?);"
        ),
        GET_ACTIVE_SCHEDULE(
                "select A.* "
                + "from iot_db.Schedule as A "
                + "join iot_db.Thermostat_Device as B on A.Schedule_ID = B.Active_Schedule "
                + "where B.Serial_Number = ?;"
        ),

        GET_SCHEDULE_BY_SERIAL(
                "select * from iot_db.Schedule S join iot_db.Thermostat_Device TD on S.Schedule_ID = TD.Active_Schedule where Serial_Number = ?;"
        ),

        NEW_SCHEDULE(
                "insert into iot_db.Schedule values (?, ?);"
        ),
        SET_ACTIVE_SCHEDULE(
                "update iot_db.Thermostat_Device set Active_Schedule = ? where Serial_Number = ?;"
        ),
        GET_SCHEDULE_ID_BY_DATA(
                "select Schedule_ID from iot_db.Schedule where Device_Serial = ? and Schedule_Data = ?;"
        ),
        GET_LOCATION_BY_SERIAL(
                "select L.* from iot_db.Location as L join iot_db.Thermostat_Device as TD on L.Location_ID = TD.Location_ID where Serial_Number = ?;"
        )
        ;
        public final String sql;

        Query(String sql) {
            this.sql = sql;
        }
    }

    //dummy function for secureGetSchedule bc idk how to use it with the token
    public List<Schedule> getSchedules(String scheduleID, boolean onlyActive) {
        if (onlyActive) {
            final Connection connection;
            connection = Dao.getDBConnection();
            try {
                final PreparedStatement statement = connection.prepareStatement(Query.GET_ACTIVE_SCHEDULE.sql);
                statement.setString(1, scheduleID);
                final JsonArray jsonResults = Utils.rsToJSON(statement.executeQuery());
                //System.out.println(jsonResults.size());
                System.out.println(scheduleID);
                System.out.println(jsonResults.get(0));
                if (jsonResults.size() == 0) return null;
                System.out.println(jsonResults.get(0).toString());
                Schedule s = Dao.GSON.fromJson(jsonResults.get(0).toString(), Schedule.class);
                return Collections.singletonList(
                        Dao.GSON.fromJson(jsonResults.get(0).toString().toLowerCase(), Schedule.class)
                );
            } catch (Exception e) {
                e.printStackTrace(); // TODO: proper logging
            }
            return null;
        }
        return null;
    }

    public SimpleSchedule getScheduleByDevice(String serial) {
        final Connection connection;
        if ((connection = Dao.getDBConnection()) == null) return null;
        try {
            PreparedStatement statement = connection.prepareStatement(Query.GET_SCHEDULE_BY_SERIAL.sql);
            statement.setString(1, serial);
            JsonArray results = Utils.rsToJSON(statement.executeQuery());
            if (results.size() == 0) return null;
            Schedule schedule = Dao.GSON.fromJson(results.get(0).toString().toLowerCase(), Schedule.class);
            ScheduleData data = Dao.GSON.fromJson(schedule.scheduleData.toLowerCase(), ScheduleData.class);
            SimpleSchedule result = null;
                // handle the new feature
                // get location of thermostat
                PreparedStatement locQuery = connection.prepareStatement(Query.GET_LOCATION_BY_SERIAL.sql);
                locQuery.setString(1, serial);
                JsonArray locRes = Utils.rsToJSON(locQuery.executeQuery());
                if (locRes.size() == 0) return null;
            System.out.println(locRes.get(0));
                Location location = Dao.GSON.fromJson(locRes.get(0).toString().toLowerCase(), Location.class);
                // get forecast first
                Forecast forecast = new ForecastDao().getForecastByLocation(location);
                // decide on the mode
                Integer temperature = ForecastDao.extractTempFromForecast(forecast);
                if (temperature == null) return null;
                // assume that first element is threshold
                //if (data.isBudget) {
                    if (temperature < data.temperatures[0] - TEMP_TOLERANCE) {
                        result = new SimpleSchedule(data.temperatures[data.isBudget? 1 : 0], "heat"); // for heat, second element is minimum
                    } else if (temperature > data.temperatures[0] + TEMP_TOLERANCE){
                        result = new SimpleSchedule(data.temperatures[data.isBudget? 2 : 0], "cool"); // for cool, third element is maximum
                    } else {
                        result = new SimpleSchedule(data.temperatures[0], "off");
                    }
                //} else {
                    /*
                    result = new SimpleSchedule(
                            data.temperatures[(int)(System.currentTimeMillis() % 86400000) / 1800000],
                            "auto"
                    );

                     */
                //}
                // format new simpleschedule

            return result;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    //dummy function for secureCreateSchedule bc idk how to use it with the token
    public boolean createSchedule(Schedule schedule) {
        final Connection connection;
        if ((connection = Dao.getDBConnection()) == null) return false;
        try {
            final PreparedStatement statement = connection.prepareStatement(
                    Query.NEW_SCHEDULE.sql
            );

            statement.setString(1, schedule.scheduleData);
            statement.setString(2, schedule.deviceSerial);

            final JsonArray json = Utils.rsToJSON(statement.executeQuery());
            if (json.size() == 0) return false;
            return GSON.fromJson(json, new TypeToken<List<Schedule>>() {}.getType()); // use this syntax for lists
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); // TODO: proper logging
        }
        return false;
    }

    List<Schedule> secureGetSchedules(String serial, boolean onlyActive, Token token) {
        final Connection connection;
        if ((connection = Dao.getDBConnection(token)) == null) return null;
        try {
            final PreparedStatement statement = connection.prepareStatement(
                    onlyActive? Query.SEC_GET_ACTIVE_SCHEDULE.sql : Query.SEC_GET_SCHEDULES.sql
            );

            statement.setString(1, serial);
            statement.setString(2, token.accountID);
            statement.setString(3, token.accountID);

            final JsonArray json = Utils.rsToJSON(statement.executeQuery());
            if (json.size() == 0) return null;
            return GSON.fromJson(json, new TypeToken<List<Schedule>>() {}.getType()); // use this syntax for lists
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); // TODO: proper logging
        }
        return null;
    }

    public boolean secureCreateSchedule(Schedule schedule, Token token) {
        return false; // TODO: implement
    }

    @Override
    public void close() throws IOException {
        // TODO
    }
}
