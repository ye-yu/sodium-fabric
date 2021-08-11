package me.jellysquid.mods.sodium.client.resource.json;

import com.google.gson.annotations.SerializedName;

public class SamplerJson {
    @SerializedName("name")
    private final String name;

    @SerializedName("type")
    private final String type;

    @SerializedName("texture")
    private final String texture;

    public SamplerJson(String name, String type, String binding) {
        this.name = name;
        this.type = type;
        this.texture = binding;
    }

    public String getTexture() {
        return this.texture;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }
}
