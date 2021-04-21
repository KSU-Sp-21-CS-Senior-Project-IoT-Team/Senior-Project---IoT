package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth;

import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Device;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.User;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.models.AccountLoginRecord;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.models.LoginCredentials;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Security;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Utils;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;

import static net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Utils.sanitize;

public class Registrar {
    private static final String ACCOUNT_INSERT =
            "insert into iot_db.Account(account_id, type, password) "
            + "values('%s', '%s', '%s');";

    private static final String USER_INSERT =
            "insert into iot_db.User(user_id, account_id, email) "
            + "values('%s', '%s', '%s');";

    private static final String DEVICE_INSERT =
            "insert into iot_db.Thermostat_Device(serial_number, account_id) values('%s', '%s');";

    private static final SecureRandom RANDOM = new SecureRandom();

    public boolean registerNewUser(User user, LoginCredentials credentials, Connection dbConn, Security security) {
        // sanitize inputs
        final String sanitizedUsername, sanitizedEmail;
        sanitizedUsername = sanitize(user.userID);
        sanitizedEmail = sanitize(user.email);

        // should probably check that the email exists or something

        // generate new account_id
        final AccountLoginRecord record = security.createLoginRecord(credentials.password);

        try {
            // write to database
            dbConn.prepareStatement(String.format(
                    ACCOUNT_INSERT,
                    record.accountID, "user", record.password
            )).execute();

            dbConn.prepareStatement(String.format(
                    USER_INSERT,
                    sanitizedUsername, record.accountID, sanitizedEmail
            )).execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: proper logging
        }
        return false;
    }

    /**
     * Login account_id of
     * @return
     */
    public boolean registerNewDevice(Device device, LoginCredentials credentials, Connection dbConn, Security security) {
        final String sanitizedSerial = Utils.sanitize(device.serialNumber);
        final String sanitizedPassword = Utils.sanitize(credentials.password);

        final AccountLoginRecord record = security.createLoginRecord(sanitizedPassword);

        try {
            dbConn.prepareStatement(String.format(
                    ACCOUNT_INSERT,
                    record.accountID, "device", record.password
            )).execute();

            dbConn.prepareStatement(String.format(
                    DEVICE_INSERT,
                    sanitizedSerial, record.accountID
            )).execute();

            return true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); // TODO: proper logging
        }

        return true;
    }
}
