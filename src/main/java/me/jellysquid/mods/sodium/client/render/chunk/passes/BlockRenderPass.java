package me.jellysquid.mods.sodium.client.render.chunk.passes;

import me.jellysquid.mods.sodium.client.render.chunk.shader.ShaderPipelineDescription;
import net.minecraft.client.render.RenderLayer;

public record BlockRenderPass(RenderLayer layer, boolean translucent,
                              ShaderPipelineDescription pipelineDescription) {

    public boolean isTranslucent() {
        return this.translucent;
    }

    public boolean isSolid() {
        return !this.translucent;
    }

    public RenderLayer getLayer() {
        return this.layer;
    }

    @Deprecated
    public void endDrawing() {
        this.layer.endDrawing();
    }

    @Deprecated
    public void startDrawing() {
        this.layer.startDrawing();
    }
}
