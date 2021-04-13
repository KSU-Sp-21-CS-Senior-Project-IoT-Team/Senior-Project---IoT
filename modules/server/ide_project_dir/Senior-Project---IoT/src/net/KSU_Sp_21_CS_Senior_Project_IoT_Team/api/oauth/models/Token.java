package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth.models;

public class Token {
    public final String token;
    public final String accountID;
    public final long expiryTime;

    public Token(String token, String accountID, long expiryTime) {
        this.token = token;
        this.accountID = accountID;
        this.expiryTime = expiryTime;
    }
}
