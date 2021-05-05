package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api;

import java.util.HashMap;
import java.util.Map;

public enum HTTPMethod {
    GET,
    POST,
    PUT,
    DELETE,
    OPTIONS,
    ;

    private static final Map<String, HTTPMethod> STRING_LOOKUP_MAP = new HashMap<>();
    private static volatile boolean IS_INIT_STRING_LOOKUP_MAP = false;

    /**
     *
     * @param name
     * @return
     */
    public static HTTPMethod fromString(String name) {
        synchronized (STRING_LOOKUP_MAP) {
            if (!IS_INIT_STRING_LOOKUP_MAP) {
                for (HTTPMethod method : values())
                    STRING_LOOKUP_MAP.put(method.name(), method);
                IS_INIT_STRING_LOOKUP_MAP = true;
            }
            return STRING_LOOKUP_MAP.get(name);
        }
    }
}
