package me.jellysquid.mods.sodium.client.gl.shader.uniform;

import me.jellysquid.mods.sodium.client.gl.buffer.GlBuffer;
import me.jellysquid.mods.sodium.client.gl.shader.GlProgram;
import org.lwjgl.opengl.GL32C;

public class GlUniformBuffer extends GlUniform {
    public GlUniformBuffer(int index) {
        super(index);
    }

    public void set(GlBuffer buffer) {
        GL32C.glBindBufferBase(GL32C.GL_UNIFORM_BUFFER, this.index, buffer.handle());
    }
}
