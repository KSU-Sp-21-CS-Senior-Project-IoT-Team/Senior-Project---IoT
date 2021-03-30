package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

import java.util.HashMap;
import java.util.Map;

public interface APIModel {
    Map<Class<? extends APIModel>, APIModel> CHILD_DEFAULT_MAP = new HashMap<>();

    static <T extends APIModel> T getDefault(Class<T> type) {
        try {
            Class.forName(type.getName());
            return (T) CHILD_DEFAULT_MAP.get(type);
        } catch (ClassCastException castException) {
            System.err.println("Internal error with child default mappings!"); // TODO change to logging at some point
        } catch (ClassNotFoundException cnfex) {
            cnfex.printStackTrace();
            return null;
        }
        return null;
    }

    static <T extends APIModel> void registerDefault(Class<T> type, T dfault) {
        CHILD_DEFAULT_MAP.put(type, dfault);
    }
}
