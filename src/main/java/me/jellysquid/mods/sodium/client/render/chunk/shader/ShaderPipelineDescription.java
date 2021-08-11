package me.jellysquid.mods.sodium.client.render.chunk.shader;

import me.jellysquid.mods.sodium.client.gl.shader.ShaderConstants;
import me.jellysquid.mods.sodium.client.gl.shader.ShaderType;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public record ShaderPipelineDescription(Identifier name,
                                        Map<ShaderType, Identifier> shaders,
                                        List<UniformDescription> uniforms,
                                        List<SamplerDescription> samplers,
                                        ShaderConstants constants) {
}
