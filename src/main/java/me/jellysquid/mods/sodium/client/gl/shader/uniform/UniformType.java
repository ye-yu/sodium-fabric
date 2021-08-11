package me.jellysquid.mods.sodium.client.gl.shader.uniform;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.apache.commons.lang3.Validate;

import java.util.Map;
import java.util.function.IntFunction;

public class UniformType<T extends GlUniform> {
    private static final Map<String, UniformType<?>> TYPES_BY_NAME = new Object2ObjectOpenHashMap<>();

    public static final UniformType<GlUniformFloat> FLOAT = registerType("float", GlUniformFloat.class, GlUniformFloat::new);
    public static final UniformType<GlUniformInt> INT = registerType("int", GlUniformInt.class, GlUniformInt::new);
    public static final UniformType<GlUniformVec4> VEC4 = registerType("vec4", GlUniformVec4.class, GlUniformVec4::new);
    public static final UniformType<GlUniformMatrix4> MATRIX4 = registerType("mat4", GlUniformMatrix4.class, GlUniformMatrix4::new);
    public static final UniformType<GlUniformBuffer> BUFFER = registerType("buffer", GlUniformBuffer.class, GlUniformBuffer::new);

    private final Class<T> type;
    private final IntFunction<T> factory;

    public UniformType(Class<T> type, IntFunction<T> factory) {
        this.type = type;
        this.factory = factory;
    }

    public Class<T> getType() {
        return this.type;
    }

    public IntFunction<T> getFactory() {
        return this.factory;
    }

    public static UniformType<?> byName(String name) {
        return Validate.notNull(TYPES_BY_NAME.get(name));
    }

    private static <T extends GlUniform> UniformType<T> registerType(String name, Class<T> type, IntFunction<T> factory) {
        UniformType<T> uniformType = new UniformType<>(type, factory);
        TYPES_BY_NAME.put(name, uniformType);

        return uniformType;
    }
}
