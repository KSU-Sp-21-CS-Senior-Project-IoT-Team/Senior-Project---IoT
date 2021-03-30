package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

public class Schedule implements APIModel {
    static { // TODO: set values for default testing instance
        APIModel.registerDefault(Schedule.class, new Schedule(
                "8888",
                "whatever schedule data we need",
                "FFFF"
        ));
    }

    public final String scheduleID;
    public final String scheduleData; // TODO: implement this data
    public final String deviceID;

    public Schedule(String scheduleID, String scheduleData, String deviceID) {
        this.scheduleID = scheduleID;
        this.scheduleData = scheduleData;
        this.deviceID = deviceID;
    }
}
