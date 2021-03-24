package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth.models;

public class RefreshToken extends Token {
    public RefreshToken(long token, String username, long expiryTime) {
        super(token, username, expiryTime);
    }
}
