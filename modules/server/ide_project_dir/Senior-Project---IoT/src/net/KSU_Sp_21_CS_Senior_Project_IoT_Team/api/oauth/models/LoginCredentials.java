package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth.models;

public class LoginCredentials {
    public final String username;
    public final String password;

    public LoginCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
