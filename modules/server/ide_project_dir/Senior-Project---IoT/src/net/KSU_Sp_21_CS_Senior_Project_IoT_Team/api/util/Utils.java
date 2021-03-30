package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Utils {
    public static List<String> getPathParts(URI uri) {
        List<String> list = new LinkedList<>(Arrays.asList(uri.getRawPath().split("/")));
        list.remove(list.get(0));
        return list;
    }

    public static Map<String, String> parseQueryString(String queryString) {
        return null; // TODO: implement
    }

    public static void sendInternalServerError(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(500, -1);
    }
}
