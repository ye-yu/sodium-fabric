package me.jellysquid.mods.sodium.client.resource.shader;

import com.google.gson.annotations.SerializedName;

public class ShaderPackJson {
    @SerializedName("name")
    public final String name;

    public ShaderPackJson(String name) {
        this.name = name;
    }
}
