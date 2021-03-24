package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

public class Schedule {
    public final String scheduleID;
    public final String scheduleData; // TODO: implement this data
    public final String deviceID;

    public Schedule(String scheduleID, String scheduleData, String deviceID) {
        this.scheduleID = scheduleID;
        this.scheduleData = scheduleData;
        this.deviceID = deviceID;
    }
}
