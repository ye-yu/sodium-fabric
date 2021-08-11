package me.jellysquid.mods.sodium.client.resource.json;

import com.google.gson.annotations.SerializedName;

public class RenderPassJson {
    @SerializedName("name")
    private String name;

    @SerializedName("layer")
    private String layer;

    @SerializedName("translucent")
    private boolean translucent;

    @SerializedName("shader")
    private RenderPassPipelineJson shader;

    public String getLayer() {
        return this.layer;
    }

    public String getName() {
        return this.name;
    }

    public boolean isTranslucent() {
        return this.translucent;
    }

    public RenderPassPipelineJson getShader() {
        return this.shader;
    }
}
