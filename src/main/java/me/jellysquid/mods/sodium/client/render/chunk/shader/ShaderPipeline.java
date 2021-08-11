package me.jellysquid.mods.sodium.client.render.chunk.shader;

import me.jellysquid.mods.sodium.client.gl.shader.GlProgram;
import me.jellysquid.mods.sodium.client.gl.shader.GlShader;
import me.jellysquid.mods.sodium.client.gl.shader.ShaderLoader;
import me.jellysquid.mods.sodium.client.gl.shader.ShaderType;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniform;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniformInt;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.UniformType;
import me.jellysquid.mods.sodium.client.render.uniform.UniformBinder;
import me.jellysquid.mods.sodium.client.render.uniform.UniformRegistry;
import me.jellysquid.mods.sodium.client.render.uniform.UniformStage;
import me.jellysquid.mods.sodium.client.resource.ResourceResolver;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShaderPipeline<T> {
    private final GlProgram program;
    private final Map<UniformStage, List<UniformBinder<T>>> uniforms;
    private final List<SamplerBinding> samplers;

    private ShaderPipeline(GlProgram program, List<UniformBinder<T>> uniforms, List<SamplerBinding> samplers) {
        this.program = program;
        this.uniforms = uniforms.stream()
                .collect(Collectors.groupingBy(UniformBinder::getStage, () -> new EnumMap<>(UniformStage.class), Collectors.toList()));
        this.samplers = samplers;
    }

    public void bind(T obj) {
        this.program.bind();

        for (UniformBinder<T> uniform : this.getUniformForStage(UniformStage.PER_FRAME)) {
            uniform.upload(obj);
        }

        for (SamplerBinding sampler : this.samplers)  {
            sampler.upload();
        }
    }

    public void upload(T info) {
        for (UniformBinder<T> uniform : this.getUniformForStage(UniformStage.PER_DRAW)) {
            uniform.upload(info);
        }
    }

    public void unbind() {
        this.program.unbind();
    }

    public Iterable<UniformBinder<T>> getUniformForStage(UniformStage stage) {
        return this.uniforms.get(stage);
    }

    public static <T> ShaderPipeline<T> compile(ResourceResolver resolver, UniformRegistry<T> registry, TextureRegistry textures, ShaderPipelineDescription pipelineDescription) {
        GlProgram program = createProgram(resolver, pipelineDescription);
        List<UniformBinder<T>> uniforms = new ArrayList<>();

        for (UniformDescription uniformDescription : pipelineDescription.uniforms()) {
            int index = uniformDescription.type() == UniformType.BUFFER ? program.getBufferBlockIndex(uniformDescription.name()) : program.getUniformIndex(uniformDescription.name());
            GlUniform uniform = uniformDescription.type()
                    .getFactory()
                    .apply(index);

            uniforms.add(registry.linkUniform(uniformDescription.binder(), uniform));
        }

        List<SamplerBinding> samplers = new ArrayList<>();

        for (SamplerDescription samplerDescription : pipelineDescription.samplers()) {
            int index = program.getUniformIndex(samplerDescription.name());
            GlUniformInt uniform = UniformType.INT
                    .getFactory()
                    .apply(index);

            samplers.add(new SamplerBinding(uniform, textures.getTextureId(samplerDescription.texture())));
        }

        return new ShaderPipeline<>(program, uniforms, samplers);
    }

    private static GlProgram createProgram(ResourceResolver resolver, ShaderPipelineDescription description) {
        List<GlShader> shaders = new ArrayList<>();

        try {
            try {
                for (Map.Entry<ShaderType, Identifier> entry : description.shaders().entrySet()) {
                    shaders.add(ShaderLoader.loadShader(resolver, entry.getKey(), entry.getValue(), description.constants()));
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to load shaders for pipeline " + description.name(), e);
            }

            GlProgram.Builder builder = GlProgram.builder(description.name());

            for (GlShader shader : shaders) {
                builder.attachShader(shader);
            }

            builder.bindAttribute("a_Pos", ChunkShaderBindingPoints.ATTRIBUTE_POSITION_ID);
            builder.bindAttribute("a_Color", ChunkShaderBindingPoints.ATTRIBUTE_COLOR);
            builder.bindAttribute("a_TexCoord", ChunkShaderBindingPoints.ATTRIBUTE_BLOCK_TEXTURE);
            builder.bindAttribute("a_LightCoord", ChunkShaderBindingPoints.ATTRIBUTE_LIGHT_TEXTURE);
            builder.bindFragmentData("fragColor", ChunkShaderBindingPoints.FRAG_COLOR);

            return builder.link();
        } finally {
            shaders.forEach(GlShader::delete);
        }
    }

    public void delete() {
        this.program.delete();
    }
}
