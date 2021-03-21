package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util;

import java.util.function.Function;

public interface Factory<T> {
    T create();

    /**
     * Should be consistent and unique for each implementing class!
     */
    Function<String, Boolean> matcher();
}
