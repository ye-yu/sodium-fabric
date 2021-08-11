package me.jellysquid.mods.sodium.client.resource.json;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.Map;

public class RenderPassPipelineJson {
    @SerializedName("source")
    private String source;

    @SerializedName("constants")
    private Map<String, String> constants = Collections.emptyMap();

    public String getSource() {
        return this.source;
    }

    public Map<String, String> getConstants() {
        return this.constants;
    }
}
