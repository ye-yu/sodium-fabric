package me.jellysquid.mods.sodium.client.gl.shader.uniform;

import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL30C;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

public class GlUniformMatrix4 extends GlUniform {
    public GlUniformMatrix4(int index) {
        super(index);
    }

    public void set(Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buf = stack.callocFloat(16);
            value.writeColumnMajor(buf);

            GL30C.glUniformMatrix4fv(this.index, false, buf);
        }
    }
}
