package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

public class Device {
    public final String deviceID;
    public final String serialNumber;

    public Device(String deviceID, String serialNumber) {
        this.deviceID = deviceID;
        this.serialNumber = serialNumber;
    }
}
