package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

public class ScheduleCost {
    public final String costID;
    public final String scheduleID;
    public final String deviceID;
    public final String forecastID;
    public final boolean isPrediction;
    public final String date;
    public final double amount;
    public final double accuracy;

    public ScheduleCost(String costID, String scheduleID, String deviceID, String forecastID, boolean isPrediction, String date, double amount, double accuracy) {
        this.costID = costID;
        this.scheduleID = scheduleID;
        this.deviceID = deviceID;
        this.forecastID = forecastID;
        this.isPrediction = isPrediction;
        this.date = date;
        this.amount = amount;
        this.accuracy = accuracy;
    }
}
