package me.jellysquid.mods.sodium.client.resource.providers;

import me.jellysquid.mods.sodium.client.resource.ResourceProvider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipResourceProvider implements ResourceProvider {
    private final ZipFile zipFile;

    public ZipResourceProvider(Path path) {
        try {
            this.zipFile = new ZipFile(path.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Failed to open ZIP file at " + path, e);
        }
    }

    @Override
    public InputStream open(String path) throws IOException {
        ZipEntry entry = this.zipFile.getEntry(path);

        if (entry == null) {
            throw new FileNotFoundException(path);
        }

        return this.zipFile.getInputStream(entry);
    }

    @Override
    public String toString() {
        return "ZipResourceLoader{zipFile=%s}".formatted(zipFile);
    }
}
