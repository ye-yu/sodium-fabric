package me.jellysquid.mods.sodium.client.resource.shader;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.resource.*;
import me.jellysquid.mods.sodium.client.resource.providers.EmbeddedResourceProvider;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ShaderPackManager {
    private final Path folder;

    public ShaderPackManager(Path folder) {
        this.folder = folder;
    }

    public static ShaderPackManager defaultLocation() {
        Path gameDir = FabricLoader.getInstance().getGameDir().toAbsolutePath();
        Path shaderPackDir = gameDir.resolve("shaderpacks");

        if (!Files.exists(shaderPackDir)) {
            try {
                Files.createDirectories(shaderPackDir);
            } catch (IOException e) {
                SodiumClientMod.logger().warn("Failed to create shader packs directory: " + shaderPackDir);
            }

        } else if (!Files.isDirectory(shaderPackDir)) {
            throw new RuntimeException("Shader pack directory is a file: " + shaderPackDir);
        }

        return new ShaderPackManager(shaderPackDir);
    }

    public ResourceResolver createResourceResolver() {
        List<NamedResourceProvider> loaders = new ArrayList<>();
        loaders.addAll(loadInternalResourceResolvers());
        loaders.addAll(loadExternalResourceResolvers());

        return new ResourceResolver(loaders);
    }

    private Collection<? extends NamedResourceProvider> loadInternalResourceResolvers() {
        return List.of(new EmbeddedResourceProvider("sodium"));
    }

    private Collection<? extends NamedResourceProvider> loadExternalResourceResolvers() {
        if (!Files.isDirectory(this.folder)) {
            return Collections.emptyList();
        }

        try {
            return Files.walk(this.folder, 1)
                    .filter(path -> !path.equals(this.folder))
                    .map(ShaderPackManager::setupShaderPackResourceLoader)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to retrieve shader packs in directory", e);
        }
    }

    private static ShaderPackResourceProvider setupShaderPackResourceLoader(Path path) {
        try {
            return new ShaderPackResourceProvider(ResourceProvider.open(path));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load shader pack: " + path);
        }
    }

}
