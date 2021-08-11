package me.jellysquid.mods.sodium.client.render.chunk;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import me.jellysquid.mods.sodium.client.gl.attribute.GlVertexFormat;
import me.jellysquid.mods.sodium.client.gl.buffer.GlBuffer;
import me.jellysquid.mods.sodium.client.gl.device.RenderDevice;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.UniformType;
import me.jellysquid.mods.sodium.client.model.vertex.type.ChunkVertexType;
import me.jellysquid.mods.sodium.client.render.chunk.format.ChunkMeshAttribute;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPass;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPassManager;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ShaderPipeline;
import me.jellysquid.mods.sodium.client.render.chunk.shader.TextureRegistry;
import me.jellysquid.mods.sodium.client.render.uniform.UniformRegistry;
import me.jellysquid.mods.sodium.client.render.uniform.UniformStage;
import me.jellysquid.mods.sodium.client.resource.ResourceResolver;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

import java.util.Map;

public abstract class ShaderChunkRenderer implements ChunkRenderer {
    protected final Map<BlockRenderPass, ShaderPipeline<ChunkRenderInfo>> pipelines = new Object2ObjectOpenHashMap<>();

    protected final ChunkVertexType vertexType;
    protected final GlVertexFormat<ChunkMeshAttribute> vertexFormat;

    protected final RenderDevice device;

    protected ResourceResolver resourceResolver;

    public ShaderChunkRenderer(RenderDevice device, ResourceResolver resourceResolver, ChunkVertexType vertexType, BlockRenderPassManager renderPassManager) {
        this.device = device;
        this.resourceResolver = resourceResolver;
        this.vertexType = vertexType;
        this.vertexFormat = vertexType.getCustomVertexFormat();

        this.compilePipelines(renderPassManager.getRenderPasses());
    }

    private void compilePipelines(Iterable<BlockRenderPass> passes) {
        UniformRegistry<ChunkRenderInfo> uniformRegistry = new UniformRegistry<>();

        uniformRegistry.registerSimpleValue(new Identifier("minecraft", "fog_color"), UniformStage.PER_FRAME, UniformType.VEC4,
                (uniform, data) -> uniform.set(RenderSystem.getShaderFogColor()));
        uniformRegistry.registerSimpleValue(new Identifier("minecraft", "fog_start"), UniformStage.PER_FRAME, UniformType.FLOAT,
                (uniform, data) -> uniform.set(RenderSystem.getShaderFogStart()));
        uniformRegistry.registerSimpleValue(new Identifier("minecraft", "fog_end"), UniformStage.PER_FRAME, UniformType.FLOAT,
                (uniform, data) -> uniform.set(RenderSystem.getShaderFogEnd()));

        uniformRegistry.registerSimpleValue(new Identifier("builtin", "projection_matrix"), UniformStage.PER_FRAME, UniformType.MATRIX4,
                (uniform, data) -> uniform.set(RenderSystem.getProjectionMatrix()));
        uniformRegistry.registerSimpleValue(new Identifier("builtin", "model_view_matrix"), UniformStage.PER_DRAW, UniformType.MATRIX4,
                (uniform, data) -> uniform.set(data.modelViewMatrix));

        uniformRegistry.registerSimpleValue(new Identifier("sodium", "position_scale"), UniformStage.PER_FRAME, UniformType.FLOAT,
                (uniform, data) -> uniform.set(this.vertexType.getModelScale()));
        uniformRegistry.registerSimpleValue(new Identifier("sodium", "texture_scale"), UniformStage.PER_FRAME, UniformType.FLOAT,
                (uniform, data) -> uniform.set(this.vertexType.getTextureScale()));

        uniformRegistry.registerSimpleValue(new Identifier("sodium", "chunk_draw_params"), UniformStage.PER_FRAME, UniformType.BUFFER,
                (uniform, data) -> uniform.set(data.drawParamsBuffer));

        uniformRegistry.registerSimpleValue(new Identifier("sodium", "world_time"), UniformStage.PER_FRAME, UniformType.FLOAT,
                (uniform, data) -> uniform.set(data.time));
        for (BlockRenderPass pass : passes) {
            this.pipelines.put(pass, ShaderPipeline.compile(this.resourceResolver, uniformRegistry, TextureRegistry.INSTANCE, pass.pipelineDescription()));
        }
    }

    @Override
    public void delete() {
        this.pipelines.values()
                .forEach(ShaderPipeline::delete);
        this.pipelines.clear();
    }

    @Override
    public ChunkVertexType getVertexType() {
        return this.vertexType;
    }

    public static class ChunkRenderInfo {
        public Matrix4f modelViewMatrix = null;
        public GlBuffer drawParamsBuffer = null;
        public float time = 0.0f;
    }
}
