package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

import com.google.gson.annotations.SerializedName;

public class ScheduleInput implements APIModel {
    static { // TODO: set values for default testing instance
        APIModel.registerDefault(ScheduleInput.class, new ScheduleInput(
                "string",
                0x8888,
                0x8888,
                0x8888
        ));
    }

    @SerializedName("serial")
    public final String serial;

    @SerializedName("schedule")
    public final Integer schedule;

    @SerializedName("location")
    public final Integer location;

    @SerializedName("model")
    public final Integer model;

    public ScheduleInput(String serial, Integer schedule, Integer location, Integer model) {
        this.serial = serial;
        this.schedule = schedule;
        this.location = location;
        this.model = model;
    }
}
