package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

import com.google.gson.annotations.SerializedName;

public class User implements APIModel {
    static { // TODO: set values for default testing instance
        APIModel.registerDefault(User.class, new User(
                "user123",
                "8888",
                "user1234@mail.com"
        ));
    }

    @SerializedName("user_id")
    public final String userID;

    @SerializedName("account_id")
    public final String accountID;

    @SerializedName("email")
    public final String email;

    public User(String userID, String accountID, String email) {
        this.userID = userID;
        this.accountID = accountID;
        this.email = email;
    }
}
