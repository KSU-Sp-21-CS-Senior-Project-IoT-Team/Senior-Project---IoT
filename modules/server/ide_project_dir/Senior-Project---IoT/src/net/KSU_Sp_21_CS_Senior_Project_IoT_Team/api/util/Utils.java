package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util;

import java.net.URI;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Utils {
    public static List<String> getPathParts(URI uri) {
        return new LinkedList<>(Arrays.asList(uri.getRawPath().split("/")));
    }
}
