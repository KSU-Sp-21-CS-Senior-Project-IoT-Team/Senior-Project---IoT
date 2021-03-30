package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

public class User implements APIModel {
    static { // TODO: set values for default testing instance
        APIModel.registerDefault(User.class, new User(
                "user123",
                "8888",
                "user1234@mail.com"
        ));
    }

    public final String userID;
    public final String accountID;
    public final String email;

    public User(String userID, String accountID, String email) {
        this.userID = userID;
        this.accountID = accountID;
        this.email = email;
    }
}
