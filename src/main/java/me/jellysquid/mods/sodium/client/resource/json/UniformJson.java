package me.jellysquid.mods.sodium.client.resource.json;

import com.google.gson.annotations.SerializedName;

public class UniformJson {
    @SerializedName("name")
    private final String name;

    @SerializedName("type")
    private final String type;

    @SerializedName("binding")
    private final String binding;

    public UniformJson(String name, String type, String binding) {
        this.name = name;
        this.type = type;
        this.binding = binding;
    }

    public String getBinding() {
        return this.binding;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }
}
