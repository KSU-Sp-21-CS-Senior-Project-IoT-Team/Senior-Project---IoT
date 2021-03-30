package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

public class Account implements APIModel {
    static { // TODO: set values for default testing instance
        APIModel.registerDefault(Account.class, new Account(
                "1ca2f",
                "user"
        ));
    }

    public final String accountID;
    public final String type;

    public Account(String accountID, String type) {
        this.accountID = accountID;
        this.type = type;
    }
}
