package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth.models;

public class Token {
    public final long token;
    public final String username;
    public final long expiryTime;

    public Token(long token, String username, long expiryTime) {
        this.token = token;
        this.username = username;
        this.expiryTime = expiryTime;
    }
}
