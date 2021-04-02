package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth;

import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth.models.LoginCredentials;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth.models.Token;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.sql.Connection;
import java.util.Arrays;

public class Authenticator {
    public Token authenticate(LoginCredentials credentials, Connection dbconn) {
        final char[] passwordArray = credentials.password.toCharArray();
        final byte[] salt = new byte[32]; // TODO: get this from database
        final byte[] postHash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(new PBEKeySpec(
                        passwordArray, salt, 0, passwordArray.length
                )
        );
        return null; // TODO: implement
    }
}
