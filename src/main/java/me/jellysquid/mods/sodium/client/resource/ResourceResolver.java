package me.jellysquid.mods.sodium.client.resource;

import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ResourceResolver {
    private final List<ResourceProvider> packs;
    private final Map<String, ResourceProvider> namespaces;

    public ResourceResolver(List<NamedResourceProvider> packs) {
        this.packs = Collections.unmodifiableList(packs);
        this.namespaces = packs.stream()
                .collect(Collectors.toUnmodifiableMap(NamedResourceProvider::getNamespace, Function.identity()));
    }

    public InputStream open(Identifier id) throws IOException {
        return this.getResourceLoaderForNamespace(id.getNamespace())
                .open(id.getPath());
    }

    private ResourceProvider getResourceLoaderForNamespace(String namespace) {
        ResourceProvider loader = this.namespaces.get(namespace);

        if (loader == null) {
            throw new NullPointerException("No resource loader exists for namespace: " + namespace);
        }

        return loader;
    }

    public Iterable<ResourceProvider> getResourceProviders() {
        return this.packs;
    }
}
