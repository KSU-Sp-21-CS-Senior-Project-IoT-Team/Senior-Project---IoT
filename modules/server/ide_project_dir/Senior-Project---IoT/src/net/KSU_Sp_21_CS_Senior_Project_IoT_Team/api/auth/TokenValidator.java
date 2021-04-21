package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.models.Token;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Security;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Utils;

import java.sql.Connection;

public class TokenValidator {
    private static final String QUERY_TOKEN =
            "select token_id, account_id, expiry_time from iot_db.Login_Token "
            + "where token_id = '%s' and account_id = '%s';";

    private static final String DEL_TOKEN =
            "delete from iot_db.Login_Token where token_id = '%s';";

    private static final Gson GSON = new GsonBuilder().create();

    public ValidationResult validate(Token token, Connection dbConn, Security security) {
        final String sanitizedToken = Utils.sanitize(token.token);
        final String sanitizedAccountID = Utils.sanitize(token.accountID);
        final long curTime = System.currentTimeMillis();

        try {
            final JsonArray queryRes = Utils.rsToJSON(dbConn.prepareStatement(String.format(
                    QUERY_TOKEN,
                    sanitizedToken, sanitizedAccountID
            )).executeQuery());

            // invalid token
            if (queryRes.size() == 0)
                return ValidationResult.REJECTED;

            // token is still active
            if (GSON.fromJson(queryRes.get(0), Token.class).expiryTime > curTime)
                return ValidationResult.AUTHORIZED;

            // remove expired token from the database
            dbConn.prepareStatement(String.format(DEL_TOKEN, sanitizedToken)).execute();
            return ValidationResult.EXPIRED;

        } catch (Exception ex) {
            ex.printStackTrace(); // TODO: proper logging
            return ValidationResult.REJECTED;
        }
    }
}
