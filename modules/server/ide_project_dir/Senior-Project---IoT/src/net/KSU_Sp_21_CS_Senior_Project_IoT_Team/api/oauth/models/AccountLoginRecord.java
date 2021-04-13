package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth.models;

import com.google.gson.annotations.SerializedName;

public class AccountLoginRecord {
    @SerializedName("account_id")
    public final String accountID;

    @SerializedName("password")
    public final String password;

    public AccountLoginRecord(String accountID, String password) {
        this.accountID = accountID;
        this.password = password;
    }
}
