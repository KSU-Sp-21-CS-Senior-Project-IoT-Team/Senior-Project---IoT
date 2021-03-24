package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

public class HVACModel {
    public final String modelID;
    public final String imageID;
    public final String configID;
    public final String name;

    public HVACModel(String modelID, String imageID, String configID, String name) {
        this.modelID = modelID;
        this.imageID = imageID;
        this.configID = configID;
        this.name = name;
    }
}
