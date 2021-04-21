package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.models;

import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("token_id")
    public final String token;

    @SerializedName("account_id")
    public final String accountID;

    @SerializedName("expiry_time")
    public final long expiryTime;

    public Token(String token, String accountID, long expiryTime) {
        this.token = token;
        this.accountID = accountID;
        this.expiryTime = expiryTime;
    }
}
