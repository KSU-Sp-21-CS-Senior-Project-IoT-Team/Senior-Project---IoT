package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.models.DatabaseConfig;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Base64;
import java.util.function.Supplier;

public class DBConnectionProvider implements Supplier<Connection> {
    public final DatabaseConfig config;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public DBConnectionProvider(DatabaseConfig config) {
        this.config = config;
    }

    public DBConnectionProvider(File configFile) throws IOException {
        try (FileReader in = new FileReader(configFile)) {
            DatabaseConfig config = GSON.fromJson(in, DatabaseConfig.class);
            DatabaseConfig nullSafe = new DatabaseConfig(
                    config.dbLoc,
                    config.username == null? "" : config.username,
                    config.password == null? "" : config.password,
                    config.driverClass,
                    config.aesKey == null? "" : config.aesKey
            );
            // decode base64 then encode into ascii
            this.config = new DatabaseConfig(
                    nullSafe.dbLoc,
                    nullSafe.username,
                    new String(Base64.getDecoder().decode(nullSafe.password), StandardCharsets.US_ASCII),
                    nullSafe.driverClass,
                    new String(Base64.getDecoder().decode(nullSafe.aesKey), StandardCharsets.US_ASCII)
            );
        }
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
