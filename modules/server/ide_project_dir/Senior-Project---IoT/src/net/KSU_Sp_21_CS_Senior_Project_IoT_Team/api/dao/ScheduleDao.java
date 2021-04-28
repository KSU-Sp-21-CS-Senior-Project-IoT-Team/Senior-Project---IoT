package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao;

import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.models.Token;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Device;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Schedule;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScheduleDao implements Dao {
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

        NEW_SCHEDULE(
                "insert into iot_db.Schedule values (?, ?);"
        ),
        SET_ACTIVE_SCHEDULE(
                "update iot_db.Thermostat_Device set Active_Schedule = ? where Serial_Number = ?;"
        ),
        GET_SCHEDULE_ID_BY_DATA(
                "select Schedule_ID from iot_db.Schedule where Device_Serial = ? and Schedule_Data = ?;"
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
                for (Field f : s.getClass().getDeclaredFields())
                    System.out.println(f.getType().getName() + ": " + f.get(s));
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
