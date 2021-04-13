package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

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

    public static String sanitize(String unsanitized) {
        return unsanitized.replaceAll("[\"'\\\\/(),;+=%]", "");
    }

    public static JsonArray rsToJSON(ResultSet rs) throws SQLException {
        final ResultSetMetaData rsmd = rs.getMetaData();
        JsonObject obj;
        JsonPrimitive primitive;
        final JsonArray json = new JsonArray();
        final Map<Integer, String> columnMap = new HashMap<>(rsmd.getColumnCount());
        for (int i = 0; i < rsmd.getColumnCount(); i++)
            columnMap.put(i, rsmd.getColumnName(i+1));
        while (rs.next()) {
            obj = new JsonObject();
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                primitive = switch (rsmd.getColumnType(i)) {
                    case Types.BIGINT -> new JsonPrimitive(rs.getInt(i));
                    case Types.BOOLEAN -> new JsonPrimitive(rs.getBoolean(i));
                    // TODO: add rest of types
                    default -> null;
                };
                if (primitive != null) {
                    obj.add(columnMap.get(i), primitive);
                }
            }
            json.add(obj);
        }
        return json;
    }

    /**
     * Obtained from:
     * https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
     */
    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
    public static String bytesToHex(byte[] bytes) {
        final byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.US_ASCII);
    }

    private static final byte[] HEX_ARRAY_REV = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0, 0, 0, 0, 0, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF};
    public static byte[] hexToBytes(String hex) {
        final byte[] bytes = new byte[hex.length()/2];
        final byte[] chars = hex.getBytes(StandardCharsets.US_ASCII);
        for (int i = 0; i < bytes.length; i++)
            bytes[i] = (byte) ((HEX_ARRAY_REV[chars[i*2] - '0'] << 4) + HEX_ARRAY_REV[chars[i*2+1] - '0']);
        return bytes;
    }
}
