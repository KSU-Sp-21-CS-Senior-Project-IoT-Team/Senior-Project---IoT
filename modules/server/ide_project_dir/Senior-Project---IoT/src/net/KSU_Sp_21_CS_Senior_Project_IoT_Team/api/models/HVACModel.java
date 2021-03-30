package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

public class HVACModel implements APIModel {
    static { // TODO: set values for default testing instance
        APIModel.registerDefault(HVACModel.class, new HVACModel(
                "3333",
                "4444",
                "5555",
                "An HVAC Model"
        ));
    }

    public final String modelID;
    public String imageID;
    public String configID;
    public String name;

    public HVACModel(String modelID, String imageID, String configID, String name) {
        this.modelID = modelID;
        this.imageID = imageID;
        this.configID = configID;
        this.name = name;
    }
}
