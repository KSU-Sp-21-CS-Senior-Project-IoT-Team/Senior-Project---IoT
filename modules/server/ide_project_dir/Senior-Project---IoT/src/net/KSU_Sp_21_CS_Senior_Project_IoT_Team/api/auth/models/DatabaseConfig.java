package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.models;

public class DatabaseConfig {
    public final String dbLoc;
    public final String username;
    public final String password;
    public final String driverClass;
    public final String aesKey;

    public DatabaseConfig(String dbLoc, String username, String password, String driverClass, String aesKey) {
        this.dbLoc = dbLoc;
        this.username = username;
        this.password = password;
        this.driverClass = driverClass;
        this.aesKey = aesKey;
    }
}
