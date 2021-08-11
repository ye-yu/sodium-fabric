package me.jellysquid.mods.sodium.client.resource.providers;

import me.jellysquid.mods.sodium.client.resource.NamedResourceProvider;

import java.io.InputStream;

public class EmbeddedResourceProvider implements NamedResourceProvider {
    private final String directory;
    private final String namespace;

    public EmbeddedResourceProvider(String namespace) {
        this.directory = "/assets/" + namespace + "/";
        this.namespace = namespace;
    }

    @Override
    public InputStream open(String path) {
        return EmbeddedResourceProvider.class.getResourceAsStream(this.directory + path);
    }

    @Override
    public String getNamespace() {
        return this.namespace;
    }

    @Override
    public String toString() {
        return "EmbeddedResourceLoader{directory='%s', namespace='%s'}".formatted(directory, namespace);
    }
}
