package me.jellysquid.mods.sodium.client.resource.shader;

import com.google.gson.Gson;
import me.jellysquid.mods.sodium.client.resource.NamedResourceProvider;
import me.jellysquid.mods.sodium.client.resource.ResourceProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShaderPackResourceProvider implements NamedResourceProvider {
    private static final Gson GSON = new Gson();

    private final ResourceProvider delegate;
    private final ShaderPackJson info;

    public ShaderPackResourceProvider(ResourceProvider delegate) {
        this.delegate = delegate;
        this.info = parseInfo();
    }

    private ShaderPackJson parseInfo() {
        try (InputStream in = this.delegate.open("shaderpack.json")) {
            return GSON.fromJson(new InputStreamReader(in), ShaderPackJson.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load shaderpack information for " + this.delegate, e);
        }
    }

    @Override
    public InputStream open(String path) throws IOException {
        return this.delegate.open(path);
    }

    @Override
    public String getNamespace() {
        return this.info.name;
    }
}
