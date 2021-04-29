package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

public class SimpleSchedule {
    public final Integer temperature;
    public final String mode;

    public SimpleSchedule(Integer temperature, String mode) {
        this.temperature = temperature;
        this.mode = mode;
    }
}
