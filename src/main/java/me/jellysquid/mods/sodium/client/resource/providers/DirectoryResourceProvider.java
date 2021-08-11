package me.jellysquid.mods.sodium.client.resource.providers;

import me.jellysquid.mods.sodium.client.resource.ResourceProvider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryResourceProvider implements ResourceProvider {
    private final Path path;

    public DirectoryResourceProvider(Path path) {
        this.path = path;
    }

    @Override
    public InputStream open(String path) throws IOException {
        Path file = this.path.resolve(path);

        if (!Files.exists(file)) {
            throw new FileNotFoundException(file.toString());
        }

        return Files.newInputStream(file);
    }

    @Override
    public String toString() {
        return "DirectoryResourceLoader{path=%s}".formatted(path);
    }
}
