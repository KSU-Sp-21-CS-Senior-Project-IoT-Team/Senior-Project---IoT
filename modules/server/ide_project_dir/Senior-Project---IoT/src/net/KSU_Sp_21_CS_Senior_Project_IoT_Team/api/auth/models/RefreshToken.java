package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.models;

public class RefreshToken extends Token {
    public RefreshToken(String token, String username, long expiryTime) {
        super(token, username, expiryTime);
    }
}
