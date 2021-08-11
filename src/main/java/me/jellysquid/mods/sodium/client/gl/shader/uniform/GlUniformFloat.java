package me.jellysquid.mods.sodium.client.gl.shader.uniform;

import org.lwjgl.opengl.GL30C;

public class GlUniformFloat extends GlUniform {
    public GlUniformFloat(int index) {
        super(index);
    }

    public void set(float value) {
        GL30C.glUniform1f(this.index, value);
    }
}
