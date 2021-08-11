package me.jellysquid.mods.sodium.client.render.uniform;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniform;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.UniformType;
import net.minecraft.util.Identifier;

import java.util.Map;

public class UniformRegistry<IN> {
    private final Map<Identifier, UniformBinder.Factory<IN>> binders = new Object2ObjectOpenHashMap<>();

    public <T extends GlUniform> void registerSimpleValue(Identifier id, UniformStage stage, UniformType<T> type, SimpleUniformFunction<T, IN> func) {
        this.registerBinder(id, rawUniform -> {
            final T uniform = rawUniform.asType(type);

            return new UniformBinder<>(stage) {
                @Override
                public void upload(IN input) {
                    func.apply(uniform, input);
                }
            };
        });
    }

    public void registerBinder(Identifier id, UniformBinder.Factory<IN> binder) {
        this.binders.put(id, binder);
    }

    public UniformBinder<IN> linkUniform(Identifier id, GlUniform uniform) {
        return this.binders.get(id)
                .create(uniform);
    }

    public interface SimpleUniformFunction<T, D> {
        void apply(T uniform, D data);
    }
}
