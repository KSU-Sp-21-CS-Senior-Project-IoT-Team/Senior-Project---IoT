package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

import com.google.gson.annotations.SerializedName;

public class Forecast implements APIModel {
    static { // TODO: set values for default testing instance
        APIModel.registerDefault(Forecast.class, new Forecast(
                0,
                0,
                "{}",
                0
        ));
    }

    @SerializedName("forecast_id")
    public final Integer forecastID;

    @SerializedName("date")
    public final Long date;

    @SerializedName("data_from_api")
    public final String dataJSON;

    @SerializedName("location_id")
    public final Integer locationID;


    public Forecast(Integer forecastID, long date, String dataJSON, Integer locationID) {
        this.forecastID = forecastID;
        this.date = date;
        this.dataJSON = dataJSON;
        this.locationID = locationID;
    }
}
