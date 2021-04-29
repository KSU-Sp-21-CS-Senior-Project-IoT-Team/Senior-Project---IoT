package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

import com.google.gson.annotations.SerializedName;

public class ScheduleData {
    public final Integer[] temperatures;
    @SerializedName("is_budget")
    public final Boolean isBudget;

    public ScheduleData(Integer[] temperatures, Boolean isBudget) {
        this.temperatures = temperatures;
        this.isBudget = isBudget;
    }
}
