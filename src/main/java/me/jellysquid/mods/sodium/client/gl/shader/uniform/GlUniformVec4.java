package me.jellysquid.mods.sodium.client.gl.shader.uniform;

import org.lwjgl.opengl.GL30C;

public class GlUniformVec4 extends GlUniform {
    public GlUniformVec4(int index) {
        super(index);
    }

    public void set(float[] value) {
        if (value.length != 4) {
            throw new IllegalArgumentException("value.length != 4");
        }

        GL30C.glUniform4fv(this.index, value);
    }
}
