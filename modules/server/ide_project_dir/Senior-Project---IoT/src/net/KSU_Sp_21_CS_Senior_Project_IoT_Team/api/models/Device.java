package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

import com.google.gson.annotations.SerializedName;

public class Device implements APIModel {
    static { // TODO: set values for default testing instance
        APIModel.registerDefault(Device.class, new Device(
                "FFFF",
                "EEEE",
                "DDDD",
                0xCCCC,
                0xDDDD,
                0xEEEE
        ));
    }

    @SerializedName("serial_number")
    public final String serialNumber;

    @SerializedName("account_id")
    public final String accountID;

    @SerializedName("user_id")
    public final String ownerID;

    @SerializedName("location_id")
    public final Integer locationID;

    @SerializedName("model_id")
    public final Integer hvacModelID;

    @SerializedName("active_schedule")
    public final Integer scheduleID;

    public Device(String serialNumber, String accountID, String ownerID, Integer locationID, Integer hvacModelID, Integer scheduleID) {
        this.serialNumber = serialNumber;
        this.accountID = accountID;
        this.ownerID = ownerID;
        this.locationID = locationID;
        this.hvacModelID = hvacModelID;
        this.scheduleID = scheduleID;
    }
}
