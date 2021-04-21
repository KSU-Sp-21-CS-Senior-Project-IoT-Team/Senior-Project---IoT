package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.APIHandler;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.models.Token;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.DBConnectionProvider;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Simple login system based loosely on OAuth2.
 *
 * Ideally, the Authenticator would be on a separate machine, and the TokenValidator would be with the rest of the API.
 * However, we aren't doing that here.
 */
public class AuthenticationServer extends APIHandler {
    static {
        /*
         * This line is the only one you really care about. Define a regular expression that
         * describes all of the API paths that this handler will service. For regex help,
         * see https://www.regexr.com.
         *
         * Otherwise, just make sure the bottom few lines have the name of this class.
         */
        final Pattern pattern = PATTERN = Pattern.compile("/api/((login)|((devices|users)/register))");

        final Function<String, Boolean> matcher = MATCHER = s -> pattern.matcher(s).matches();
        APIHandler.CHILD_MATCHER_MAP.put(AuthenticationServer.class, matcher);
        APIHandler.CHILD_CONS_MAP.put(AuthenticationServer.class, AuthenticationServer::new);
    }
    private static final Pattern PATTERN;
    private static final Function<String, Boolean> MATCHER;
    private static final String DEFAULT_DB_CFG_LOC = "./config/auth_db_cfg.json";
    private static DBConnectionProvider dbConnectionProvider;

    private final AuthenticationServiceProvider provider;
    private static final Gson gson = new GsonBuilder().create();

    public AuthenticationServer() {
        this(new AuthenticationServiceProvider(
                new Authenticator(),
                new TokenValidator(),
                new Registrar(),
                dbConnectionProvider
        ));
    }

    public AuthenticationServer(AuthenticationServiceProvider provider) {
        super(MATCHER);
        this.provider = provider;
    }

    @Override
    public void doPOST(HttpExchange exchange) {
        // TODO: implement login & registration HTTP comms
        final List<String> pathParts = Utils.getPathParts(exchange.getRequestURI());
        final Consumer<HttpExchange> service = switch (pathParts.get(1)) {
            case "login" -> this::doLogin;
            case "devices" -> this::doRegisterDevice;
            case "users" -> this::doRegisterUser;
            default -> super::sendNotFound;
        };
        service.accept(exchange);
    }

    private void doLogin(HttpExchange exchange) {
        final String[] rawAuthParts = new String(
                Base64.getDecoder().decode(
                        exchange.getRequestHeaders().get("Authorization").get(1)
                ),
                StandardCharsets.US_ASCII
        ).split(":");
        final Token token = provider.authenticate(
                !rawAuthParts[0].contains("-"),
                rawAuthParts[0],
                rawAuthParts[1]
        );
        if (token == null) {
            // TODO: handle bad login
            return;
        }

        final String response = gson.toJson(token, Token.class);
        try (PrintWriter out = new PrintWriter(exchange.getResponseBody())) {
            exchange.sendResponseHeaders(200, response.length());
            out.print(response);
        } catch (IOException ioException) {
            ioException.printStackTrace(); // TODO: proper logging
        }
    }

    private void doRegisterUser(HttpExchange exchange) {
        // TODO: implement registration comms
    }

    private void doRegisterDevice(HttpExchange exchange) {
        // TODO: implement registration commms
    }

    @Override
    public void close() throws IOException {
        provider.close();
    }

    public static void init() throws IOException {
        dbConnectionProvider = new DBConnectionProvider(new File(DEFAULT_DB_CFG_LOC));
    }
}
