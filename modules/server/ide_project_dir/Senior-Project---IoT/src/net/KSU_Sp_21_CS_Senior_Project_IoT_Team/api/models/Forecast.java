package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

public class Forecast implements APIModel {
    static { // TODO: set values for default testing instance
        APIModel.registerDefault(Forecast.class, new Forecast(
                "0000",
                "today",
                "{}"
        ));
    }

    public final String forecastID;
    public final String date;
    public final String dataJSON;

    public Forecast(String forecastID, String date, String dataJSON) {
        this.forecastID = forecastID;
        this.date = date;
        this.dataJSON = dataJSON;
    }
}
