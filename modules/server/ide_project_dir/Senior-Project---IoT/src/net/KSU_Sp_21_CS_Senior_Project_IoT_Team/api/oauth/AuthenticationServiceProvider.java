package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth;

import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Device;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.User;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth.models.DatabaseConfig;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth.models.LoginCredentials;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth.models.Token;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Security;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Wrapper;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AuthenticationServiceProvider implements Closeable {
    private static final long TOKEN_LIFETIME = 86400000;
    private static final int SHA_ITERATIONS = 100000;

    private final Authenticator authenticator;
    private final TokenValidator validator;
    private final Registrar registrar;
    private Connection dbConnection;
    private Security apiSecurity;

    private final Wrapper<Boolean> isAlive = new Wrapper<>(false);

    public AuthenticationServiceProvider(Authenticator authenticator, TokenValidator validator, Registrar registrar, DatabaseConfig dbConfig) {
        this.authenticator = authenticator;
        this.validator = validator;
        this.registrar = registrar;

        if (authenticator == null || validator == null || registrar == null || dbConfig == null) return;

        try {
            this.apiSecurity = new Security(TOKEN_LIFETIME, SHA_ITERATIONS, dbConfig.aesKey);
            isAlive.val = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Class.forName(dbConfig.driverClass);
            dbConnection = DriverManager.getConnection(dbConfig.dbLoc, dbConfig.username, dbConfig.password);
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace(); // TODO: proper handling
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); // TODO: proper handling
        }
    }

    public ValidationResult validate(Token token) {
        synchronized (isAlive) {
            if (!isAlive.val) {
                // TODO: proper logging
                return ValidationResult.REJECTED;
            }
        }
        if (dbConnection != null) {
            return validator.validate(token, dbConnection, apiSecurity);
        } else {
            // TODO: refresh db connection
        }
        return ValidationResult.REJECTED;
    }

    public Token authenticate(boolean isUser, String id, String password) {
        synchronized (isAlive) {
            if (!isAlive.val) {
                // TODO: logging
                return null;
            }
        }
        return authenticator.authenticate(isUser, id, password, dbConnection, apiSecurity);
    }

    public boolean register(Device device, LoginCredentials credentials) {
        synchronized (isAlive) {
            if (!isAlive.val) {
                // TODO: logging
                return false;
            }
        }
        return registrar.registerNewDevice(device, credentials, dbConnection, apiSecurity);
    }

    public boolean register(User user, LoginCredentials credentials) {
        synchronized (isAlive) {
            if (!isAlive.val) {
                // TODO: logging
                return false;
            }
        }
        return registrar.registerNewUser(user, credentials, dbConnection, apiSecurity);
    }

    @Override
    public void close() throws IOException {
        synchronized (isAlive) {
            isAlive.val = false;
        }
        // TODO: proper shutdown synchronization
        try {
            dbConnection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); // TODO: proper handling
        }
    }
}
