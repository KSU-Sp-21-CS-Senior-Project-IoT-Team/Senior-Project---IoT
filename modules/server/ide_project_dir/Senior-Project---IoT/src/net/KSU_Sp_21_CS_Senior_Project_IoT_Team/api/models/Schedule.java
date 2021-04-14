package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

import com.google.gson.annotations.SerializedName;

public class Schedule implements APIModel {
    static { // TODO: set values for default testing instance
        APIModel.registerDefault(Schedule.class, new Schedule(
                0x8888,
                "whatever schedule data we need",
                "FFFF"
        ));
    }

    @SerializedName("schedule_id")
    public final Integer scheduleID;

    @SerializedName("schedule_data")
    public final String scheduleData; // TODO: implement this data

    @SerializedName("device_serial")
    public final String deviceSerial;

    public Schedule(Integer scheduleID, String scheduleData, String deviceSerial) {
        this.scheduleID = scheduleID;
        this.scheduleData = scheduleData;
        this.deviceSerial = deviceSerial;
    }
}
