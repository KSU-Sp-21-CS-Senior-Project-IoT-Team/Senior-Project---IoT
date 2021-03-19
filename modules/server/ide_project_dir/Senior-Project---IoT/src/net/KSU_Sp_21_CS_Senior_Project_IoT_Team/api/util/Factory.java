package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util;

public interface Factory<T> {
    T create();

    /**
     * Should be consistent and unique for each implementing class!
     */
    String pathPattern();
}
