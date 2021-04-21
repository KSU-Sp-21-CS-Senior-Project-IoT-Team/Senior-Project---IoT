package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.AuthenticationServiceProvider;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.ValidationResult;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.models.DatabaseConfig;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.models.Token;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.DBConnectionProvider;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Wrapper;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;

public interface Dao extends Closeable {
    Wrapper<DBConnectionProvider> DB_CONNECTION_PROVIDER = new Wrapper<>(null);
    Wrapper<AuthenticationServiceProvider> security = new Wrapper<>(null);
    Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();
    Gson GSON = new GsonBuilder().create();

    static void setSecurity(AuthenticationServiceProvider provider) throws IOException {
        synchronized (security) {
            security.val.close();
            security.val = provider;
        }
    }

    static AuthenticationServiceProvider getSecurity() {
        synchronized (security) {
            return security.val;
        }
    }

    static void setDbConnectionProvider(File configFile) throws IOException {
        synchronized (DB_CONNECTION_PROVIDER) {
            DB_CONNECTION_PROVIDER.val = new DBConnectionProvider(configFile);
        }
    }

    static void setDbConnectionProvider(DatabaseConfig config) {
        synchronized (DB_CONNECTION_PROVIDER) {
            DB_CONNECTION_PROVIDER.val = new DBConnectionProvider(config);
        }
    }

    static Connection getDBConnection() {
        synchronized (DB_CONNECTION_PROVIDER) {
            if (DB_CONNECTION_PROVIDER.val == null) {
                return null;
            }
            return DB_CONNECTION_PROVIDER.val.get();
        }
    }

    static Connection getDBConnection(Token token) {
        return getSecurity().validate(token) != ValidationResult.AUTHORIZED ? null : getDBConnection();
    }
}
