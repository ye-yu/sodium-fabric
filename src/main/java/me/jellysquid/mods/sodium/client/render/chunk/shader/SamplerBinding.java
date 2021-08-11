package me.jellysquid.mods.sodium.client.render.chunk.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniformInt;
import org.lwjgl.opengl.GL32C;

public class SamplerBinding {
    private final GlUniformInt uniform;
    private final int textureId;

    public SamplerBinding(GlUniformInt uniform, int textureId) {
        this.uniform = uniform;
        this.textureId = textureId;
    }

    public void upload() {
        RenderSystem.activeTexture(GL32C.GL_TEXTURE0 + this.textureId);
        RenderSystem.bindTexture(RenderSystem.getShaderTexture(this.textureId));

        this.uniform.setInt(this.textureId);
    }
}
