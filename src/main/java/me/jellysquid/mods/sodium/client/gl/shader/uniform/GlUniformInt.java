package me.jellysquid.mods.sodium.client.gl.shader.uniform;

import org.lwjgl.opengl.GL30C;

public class GlUniformInt extends GlUniform {
    public GlUniformInt(int index) {
        super(index);
    }

    public void setInt(int value) {
        GL30C.glUniform1i(this.index, value);
    }
}
