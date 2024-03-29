package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao;

import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.InvalidTokenException;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.models.Token;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Device;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Schedule;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DeviceDao implements Dao {
    private enum Query {
        SEC_GET_DEVICE_BY_SERIAL(
                "select A.* "
                + "from iot_db.Thermostat_Device as A "
                + "left join iot_db.User as B on A.User_ID = B.User_ID "
                + "where Serial_Number = ? and (A.Account_ID = ? or B.Account_ID = ?);"
        ),
        GET_DEVICE_BY_SERIAL(
                "select * from iot_db.Thermostat_Device where Serial_Number = ?"
        ),
        GET_DEVICE_BY_USERNAME(
                "select * from iot_db.Thermostat_Device where User_ID = ?;"
        ),

        SEC_SET_DEVICE_ATTRIBUTES__MASTER(
                "update iot_db.Thermostat_Device as A "
                + "join iot_db.User B on B.User_ID = A.User_ID "
                + "set A.Active_Schedule = %s, "
                + "A.Location_ID = %s, "
                + "A.Model_ID = %s "
                + "where Serial_Number = ? and (A.Account_ID = ? or B.Account_ID = ?);"
        ),
        SET_DEVICE_ATTRIBUTES__SCHEDULE(
                "(select Active_Schedule from iot_db.Thermostat_Device where Serial_Number = ? limit 1)"
        ),
        SET_DEVICE_ATTRIBUTES__LOCATION(
                "(select Location_ID from iot_db.Thermostat_Device where Serial_Number = ? limit 1)"
        ),
        SET_DEVICE_ATTRIBUTES__MODEL(
                "(select Model_ID from iot_db.Thermostat_Device where Serial_Number = ? limit 1)"
        ),
        ;
        public final String sql;

        Query(String sql) {
            this.sql = sql;
        }
    }

    public Device secureGetDeviceBySerial(String serial, Token token) throws InvalidTokenException {
        //System.out.println(serial);
        //System.out.println(GSON_PRETTY.toJson(token, Token.class));
        final Connection connection;
        if ((connection = Dao.getDBConnection(token)) == null) throw new InvalidTokenException();
        try {
            final PreparedStatement statement = connection.prepareStatement(Query.SEC_GET_DEVICE_BY_SERIAL.sql);
            statement.setString(1, serial);
            statement.setString(2, token.accountID);
            statement.setString(3, token.accountID);
            final JsonArray jsonResults = Utils.rsToJSON(statement.executeQuery());
            //System.out.println(jsonResults.size());
            System.out.println(jsonResults.get(0));
            if (jsonResults.size() == 0) return null;
            System.out.println(jsonResults.get(0).toString());
            Device d = Dao.GSON.fromJson(jsonResults.get(0).toString(), Device.class);
            for (Field f : d.getClass().getDeclaredFields())
                System.out.println(f.getType().getName() + ": " + f.get(d));
            return Dao.GSON.fromJson(jsonResults.get(0).toString().toLowerCase(), Device.class);
        } catch (Exception e) {
            e.printStackTrace(); // TODO: proper logging
        }
        return null;
    }

    /**
     * Sets some or all attributes of a device
     * @param schedule the new schedule, set to null if no change
     * @param location the new location, set to null if no change
     * @param model the new model, set to null if no change
     */
    public boolean secureSetAttributes(String serial, Integer schedule, Integer location, Integer model, Token token) {
        final Connection connection;
        if ((connection = Dao.getDBConnection(token)) == null) return false;

        String scheduleSQL = schedule == null? Query.SET_DEVICE_ATTRIBUTES__SCHEDULE.sql : "?";
        String locationSQL = location == null? Query.SET_DEVICE_ATTRIBUTES__LOCATION.sql : "?";
        String modelSQL = model == null? Query.SET_DEVICE_ATTRIBUTES__MODEL.sql : "?";

        try {
            PreparedStatement statement = connection.prepareStatement(
                    String.format(
                            Query.SEC_SET_DEVICE_ATTRIBUTES__MASTER.sql,
                            scheduleSQL,
                            locationSQL,
                            modelSQL
                    )
            );

            if (schedule == null) statement.setString(1, serial);
            else statement.setInt(1, schedule);

            if (location == null) statement.setString(2, serial);
            else statement.setInt(2, location);

            if (model == null) statement.setString(3, serial);
            else statement.setInt(3, model);

            statement.setString(4, serial);
            statement.setString(5, token.accountID);
            statement.setString(6, token.accountID);

            return statement.executeUpdate() > 0;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); // TODO: proper logging
        } catch (Throwable throwable) {
            throwable.printStackTrace(); // TODO: proper logging. this shouldn't ever happen though.
        }
        return false;
    }
    //JY changed it to public since the token has to be used for secureGetDeviceBySerial. Have to change to not public.
    public Device getDeviceBySerial(String serial) {
        final Connection connection;
        connection = Dao.getDBConnection();
        try {
            final PreparedStatement statement = connection.prepareStatement(Query.GET_DEVICE_BY_SERIAL.sql);
            statement.setString(1, serial);
            final JsonArray jsonResults = Utils.rsToJSON(statement.executeQuery());
            //System.out.println(jsonResults.size());
            System.out.println(jsonResults.get(0));
            if (jsonResults.size() == 0) return null;
            System.out.println(jsonResults.get(0).toString());
            Device d = Dao.GSON.fromJson(jsonResults.get(0).toString(), Device.class);
            for (Field f : d.getClass().getDeclaredFields())
                System.out.println(f.getType().getName() + ": " + f.get(d));
            return Dao.GSON.fromJson(jsonResults.get(0).toString().toLowerCase(), Device.class);
        } catch (Exception e) {
            e.printStackTrace(); // TODO: proper logging
        }
        return null;
    }

    // dummy function.
    public Device getDevicesByUserID(String s) {
        final Connection connection;
        connection = Dao.getDBConnection();
        try {
            final PreparedStatement statement = connection.prepareStatement(Query.GET_DEVICE_BY_USERNAME.sql);
            statement.setString(1, s);
            final JsonArray jsonResults = Utils.rsToJSON(statement.executeQuery());
            //System.out.println(jsonResults.size());
            System.out.println(jsonResults.get(0));
            if (jsonResults.size() == 0) return null;
            System.out.println(jsonResults.get(0).toString());
            Device d = Dao.GSON.fromJson(jsonResults.get(0).toString(), Device.class);
            for (Field f : d.getClass().getDeclaredFields())
                System.out.println(f.getType().getName() + ": " + f.get(d));
            return Dao.GSON.fromJson(jsonResults.get(0).toString().toLowerCase(), Device.class);
        } catch (Exception e) {
            e.printStackTrace(); // TODO: proper logging
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        // TODO
    }
}
