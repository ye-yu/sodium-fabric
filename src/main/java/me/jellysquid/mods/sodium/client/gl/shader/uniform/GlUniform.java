package me.jellysquid.mods.sodium.client.gl.shader.uniform;

public abstract class GlUniform {
    protected final int index;

    protected GlUniform(int index) {
        this.index = index;
    }

    public <T extends GlUniform> T asType(UniformType<T> type) {
        return type.getType()
                .cast(this);
    }
}
