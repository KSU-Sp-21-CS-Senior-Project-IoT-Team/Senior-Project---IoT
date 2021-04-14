package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

import com.google.gson.annotations.SerializedName;

public class Location implements APIModel {
    static { // TODO: set values for default testing instance
        APIModel.registerDefault(Location.class, new Location(
                0x7777,
                "US",
                "Georgia",
                "Atlanta"
        ));
    }

    @SerializedName("location_id")
    public final Integer LocationID;

    @SerializedName("country")
    public final String country;

    @SerializedName("state")
    public final String province;

    @SerializedName("city")
    public final String city;

    public Location(Integer locationID, String country, String province, String city) {
        LocationID = locationID;
        this.country = country;
        this.province = province;
        this.city = city;
    }
}
