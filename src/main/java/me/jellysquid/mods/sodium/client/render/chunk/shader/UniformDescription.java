package me.jellysquid.mods.sodium.client.render.chunk.shader;

import me.jellysquid.mods.sodium.client.gl.shader.uniform.UniformType;
import net.minecraft.util.Identifier;

public record UniformDescription(String name, UniformType<?> type, Identifier binder) {
}
