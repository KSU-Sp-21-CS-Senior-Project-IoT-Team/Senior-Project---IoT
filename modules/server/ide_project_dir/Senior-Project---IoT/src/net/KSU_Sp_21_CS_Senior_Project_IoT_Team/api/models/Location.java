package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

public class Location {
    public final String LocationID;
    public final String country;
    public final String province;
    public final String city;

    public Location(String locationID, String country, String province, String city) {
        LocationID = locationID;
        this.country = country;
        this.province = province;
        this.city = city;
    }
}
