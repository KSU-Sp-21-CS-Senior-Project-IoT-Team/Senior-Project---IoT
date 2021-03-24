package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

public class User {
    public final String userID;
    public final String accountID;
    public final String email;

    public User(String userID, String accountID, String email) {
        this.userID = userID;
        this.accountID = accountID;
        this.email = email;
    }
}
