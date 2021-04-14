package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models;

import com.google.gson.annotations.SerializedName;

public class HVACModel implements APIModel {
    static { // TODO: set values for default testing instance
        APIModel.registerDefault(HVACModel.class, new HVACModel(
                3333,
                "4444",
                "5555",
                "An HVAC Model"
        ));
    }

    @SerializedName("model_id")
    public final Integer modelID;

    @SerializedName("image")
    public final String image;

    @SerializedName("configuration_data")
    public final String configData;

    @SerializedName("name")
    public final String name;

    public HVACModel(Integer modelID, String image, String configData, String name) {
        this.modelID = modelID;
        this.image = image;
        this.configData = configData;
        this.name = name;
    }
}
