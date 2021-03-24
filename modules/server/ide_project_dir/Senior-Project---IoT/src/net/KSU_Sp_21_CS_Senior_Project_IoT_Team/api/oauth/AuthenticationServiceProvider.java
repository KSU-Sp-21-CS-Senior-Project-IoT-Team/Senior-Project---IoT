package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth;

import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth.models.LoginCredentials;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth.models.Token;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;

public class AuthenticationServiceProvider implements Closeable {
    private final Authenticator authenticator;
    private final TokenValidator validator;
    private final Connection dbConnection;

    public AuthenticationServiceProvider(Authenticator authenticator, TokenValidator validator) {
        this.authenticator = authenticator;
        this.validator = validator;
        dbConnection = null; // TODO: set up authentication DB connection here!
    }

    public ValidationResult validate(String token) {
        return validator.validate(token, dbConnection);
    }

    public Token authenticate(LoginCredentials credentials) {
        return authenticator.authenticate(credentials, dbConnection);
    }

    @Override
    public void close() throws IOException {
        // TODO: close database connection
    }
}
