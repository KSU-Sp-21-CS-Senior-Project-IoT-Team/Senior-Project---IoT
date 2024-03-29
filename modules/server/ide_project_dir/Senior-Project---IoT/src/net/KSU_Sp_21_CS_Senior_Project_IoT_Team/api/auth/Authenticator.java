package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.models.AccountLoginRecord;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.models.Token;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Security;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Utils.sanitize;

public class Authenticator {
    private static final String USER_LOGIN_QUERY =
            "select A.account_id, A.password "
            + "from iot_db.Account as A join iot_db.User as B "
            + "on A.account_id = B.account_id "
            + "where B.user_id = '%s';";

    private static final String DEVICE_LOGIN_QUERY =
            "select A.account_id, A.password "
            + "from iot_db.Account as A join iot_db.Thermostat_Device as B "
            + "on A.account_id = B.account_id "
            + "where B.serial_number = '%s';";

    private static final String TOKEN_CREATION =
            "insert into iot_db.Login_Token(token_id, account_id, expiry_time) values('%s', '%s', '%s');";

    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public Token authenticate(boolean isUser, String id, String password, Connection dbconn, Security security) {
        final String sanitizedID = sanitize(id);
        try {
            // obtain login record from database
            final PreparedStatement query = dbconn.prepareStatement(
                    String.format((isUser? USER_LOGIN_QUERY : DEVICE_LOGIN_QUERY), sanitizedID)
            );
            final ResultSet rs = query.executeQuery();
            JsonArray arr = Utils.rsToJSON(rs);
            if (arr == null || arr.size() == 0) return null;
            final AccountLoginRecord record = GSON.fromJson(arr.get(0), AccountLoginRecord.class);
            //System.out.println(GSON.toJson(record, AccountLoginRecord.class));
            if (security.comparePassword(record.password, password)) {
                final Token token = security.createToken(record.accountID);
                //System.out.println("Token: " + GSON.toJson(token, Token.class));
                dbconn.prepareStatement(
                        String.format(TOKEN_CREATION, token.token, token.accountID, token.expiryTime)
                ).execute();
                return token;
            }
            //System.out.println("Failed to login.");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); // TODO: proper logging
        } catch (Exception e) {
            e.printStackTrace(); // TODO: proper logging
        }

        return null;
    }
}
