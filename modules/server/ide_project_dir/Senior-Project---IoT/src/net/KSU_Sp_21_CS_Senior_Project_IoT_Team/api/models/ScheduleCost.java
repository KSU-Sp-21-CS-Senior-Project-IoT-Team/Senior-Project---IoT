package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

import com.google.gson.annotations.SerializedName;

public class ScheduleCost implements APIModel {
    static { // TODO: set values for default testing instance
        APIModel.registerDefault(ScheduleCost.class, new ScheduleCost(
                0x9999,
                0x1111,
                "2222",
                0x3333,
                "false",
                "today",
                1.0f,
                0.0f
        ));
    }

    @SerializedName("cost_id")
    public final Integer costID;

    @SerializedName("schedule_id")
    public final Integer scheduleID;

    @SerializedName("serial_number")
    public final String deviceID;

    @SerializedName("forecast_id")
    public final Integer forecastID;

    @SerializedName("isprediction")
    public final Boolean isPrediction;

    @SerializedName("date")
    public final String date;

    @SerializedName("amount")
    public final Float amount;

    @SerializedName("accuracy")
    public final Float accuracy;

    public ScheduleCost(Integer costID, Integer scheduleID, String deviceID, Integer forecastID, String isPrediction, String date, Float amount, Float accuracy) {
        this.costID = costID;
        this.scheduleID = scheduleID;
        this.deviceID = deviceID;
        this.forecastID = forecastID;
        this.isPrediction = Boolean.parseBoolean(isPrediction);
        this.date = date;
        this.amount = amount;
        this.accuracy = accuracy;
    }
}
