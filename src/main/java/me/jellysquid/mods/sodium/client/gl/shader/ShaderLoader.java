package me.jellysquid.mods.sodium.client.gl.shader;

import me.jellysquid.mods.sodium.client.resource.ResourceResolver;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ShaderLoader {
    /**
     * Creates an OpenGL shader from GLSL sources. The GLSL source file should be made available on the classpath at the
     * path of `/assets/{namespace}/shaders/{path}`. User defines can be used to declare variables in the shader source
     * after the version header, allowing for conditional compilation with macro code.
     *
     * @param type The type of shader to create
     * @param shader The shader information to load
     * @return An OpenGL shader object compiled with the given user defines
     */
    public static GlShader loadShader(ResourceResolver resolver, ShaderType type, Identifier name, ShaderConstants constants) {
        String source = getShaderSource(resolver, name);
        return new GlShader(type, name, ShaderParser.parseShader(resolver, source, constants));
    }

    public static String getShaderSource(ResourceResolver resolver, Identifier name) {
        try (InputStream in = resolver.open(name)) {
            if (in == null) {
                throw new RuntimeException("Shader not found: " + name);
            }

            return IOUtils.toString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read shader source for " + name, e);
        }
    }
}
