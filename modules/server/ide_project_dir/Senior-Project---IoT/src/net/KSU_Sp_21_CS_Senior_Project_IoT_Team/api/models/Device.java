package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

public class Device implements APIModel {
    static { // TODO: set values for default testing instance
        APIModel.registerDefault(Device.class, new Device(
                "FFFF",
                "EEEE",
                "DDDD",
                "CCCC",
                "BBBB",
                "AAAA"
        ));
    }

    public final String serialNumber;
    public final String accountID;
    public final String ownerID;
    public final String locationID;
    public final String hvacModelID;
    public final String scheduleID;

    public Device(String serialNumber, String accountID, String ownerID, String locationID, String hvacModelID, String scheduleID) {
        this.serialNumber = serialNumber;
        this.accountID = accountID;
        this.ownerID = ownerID;
        this.locationID = locationID;
        this.hvacModelID = hvacModelID;
        this.scheduleID = scheduleID;
    }
}
