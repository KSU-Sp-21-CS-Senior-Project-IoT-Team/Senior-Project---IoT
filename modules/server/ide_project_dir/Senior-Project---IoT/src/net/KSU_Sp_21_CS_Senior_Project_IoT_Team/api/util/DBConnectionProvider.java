package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util;

import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth.models.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.function.Supplier;

public class DBConnectionProvider implements Supplier<Connection> {
    private final DatabaseConfig config;

    public DBConnectionProvider(DatabaseConfig config) {
        this.config = config;
    }

    @Override
    public Connection get() {
        try {
            Class.forName(config.driverClass);
            return DriverManager.getConnection(config.dbLoc, config.username, config.password);
        } catch (Exception e) {
            e.printStackTrace(); // TODO: proper logging
        }
        return null;
    }
}
