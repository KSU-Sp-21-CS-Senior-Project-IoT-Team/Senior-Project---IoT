package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.HTTPMethod;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth.models.LoginCredentials;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth.models.Token;

import java.io.*;

/**
 * Simple implementation of OAuth.
 *
 * Ideally, the Authenticator would be on a separate machine, and the TokenValidator would be with the rest of the API.
 * However, we aren't doing that here. This is not secure enough for production.
 */
public class AuthenticationServer implements HttpHandler, Closeable {
    private final AuthenticationServiceProvider provider;
    private static final Gson gson = new GsonBuilder().create();

    public AuthenticationServer(AuthenticationServiceProvider provider) {
        this.provider = provider;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException { // TODO: properly implement OAuth
        if (HTTPMethod.fromString(exchange.getRequestMethod()) == HTTPMethod.POST) {
            final LoginCredentials credentials = gson.fromJson(
                    new InputStreamReader(exchange.getRequestBody()),
                    LoginCredentials.class
            );
            if (credentials != null) {
                final Token token = provider.authenticate(credentials);
                if (token != null) {
                    try (PrintWriter out = new PrintWriter(exchange.getResponseBody())) {
                        final String response = gson.toJson(token, Token.class);
                        exchange.sendResponseHeaders(200, response.length());
                        out.print(response);
                    }
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        provider.close();
    }
}
