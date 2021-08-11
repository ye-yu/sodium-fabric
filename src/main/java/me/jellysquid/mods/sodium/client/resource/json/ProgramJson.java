package me.jellysquid.mods.sodium.client.resource.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class ProgramJson {
    @SerializedName("shaders")
    private final Map<String, String> shaders;

    @SerializedName("uniforms")
    private final List<UniformJson> uniforms;

    @SerializedName("samplers")
    private final List<SamplerJson> samplers;

    public ProgramJson(Map<String, String> shaders, List<UniformJson> uniforms, List<SamplerJson> samplers) {
        this.shaders = shaders;
        this.uniforms = uniforms;
        this.samplers = samplers;
    }

    public Map<String, String> getShaders() {
        return this.shaders;
    }

    public List<UniformJson> getUniforms() {
        return this.uniforms;
    }

    public List<SamplerJson> getSamplers() {
        return this.samplers;
    }
}
