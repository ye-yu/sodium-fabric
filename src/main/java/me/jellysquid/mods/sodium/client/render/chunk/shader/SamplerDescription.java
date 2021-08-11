package me.jellysquid.mods.sodium.client.render.chunk.shader;

import me.jellysquid.mods.sodium.client.gl.sampler.SamplerType;
import net.minecraft.util.Identifier;

public record SamplerDescription(String name, SamplerType type, Identifier texture) {
}