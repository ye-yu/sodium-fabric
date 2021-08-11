package me.jellysquid.mods.sodium.client.gl.shader;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.apache.commons.lang3.Validate;
import org.lwjgl.opengl.GL20C;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An enumeration over the supported OpenGL shader types.
 */
public enum ShaderType {
    VERTEX(GL20C.GL_VERTEX_SHADER, ".vsh"),
    FRAGMENT(GL20C.GL_FRAGMENT_SHADER, ".fsh");

    private static final Map<String, ShaderType> BY_NAME = Arrays.stream(ShaderType.values())
            .collect(Collectors.toUnmodifiableMap(e -> e.name().toLowerCase(), Function.identity()));

    public final int id;
    public final String extension;

    ShaderType(int id, String extension) {
        this.id = id;
        this.extension = extension;
    }

    public static ShaderType byName(String key) {
        return Validate.notNull(BY_NAME.get(key));
    }
}
