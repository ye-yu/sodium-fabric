package me.jellysquid.mods.sodium.client.resource;

import me.jellysquid.mods.sodium.client.resource.providers.DirectoryResourceProvider;
import me.jellysquid.mods.sodium.client.resource.providers.ZipResourceProvider;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public interface ResourceProvider {
    static ResourceProvider open(Path path) {
        if (Files.isDirectory(path)) {
            return new DirectoryResourceProvider(path);
        }

        String extension = FilenameUtils.getExtension(path.toString());

        return switch (extension) {
            case "zip" -> new ZipResourceProvider(path);
            default -> throw new RuntimeException("Unable to create resource provider: " + path);
        };
    }

    InputStream open(String path) throws IOException;
}
