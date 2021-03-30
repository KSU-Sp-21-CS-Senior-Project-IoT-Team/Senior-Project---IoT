package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

public class ScheduleCost implements APIModel {
    static { // TODO: set values for default testing instance
        APIModel.registerDefault(ScheduleCost.class, new ScheduleCost(
                "9999",
                "1111",
                "2222",
                "3333",
                false,
                "today",
                1.0,
                0.0
        ));
    }

    public final String costID;
    public final String scheduleID;
    public final String deviceID;
    public final String forecastID;
    public final Boolean isPrediction;
    public final String date;
    public Double amount;
    public Double accuracy;

    public ScheduleCost(String costID, String scheduleID, String deviceID, String forecastID, Boolean isPrediction, String date, Double amount, Double accuracy) {
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
