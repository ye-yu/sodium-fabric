package me.jellysquid.mods.sodium.client.render.uniform;

import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniform;

public abstract class UniformBinder<IN> {
    protected final UniformStage stage;

    protected UniformBinder(UniformStage stage) {
        this.stage = stage;
    }

    public abstract void upload(IN input);

    public UniformStage getStage() {
        return this.stage;
    }

    public interface Factory<IN> {
        UniformBinder<IN> create(GlUniform uniform);
    }
}
