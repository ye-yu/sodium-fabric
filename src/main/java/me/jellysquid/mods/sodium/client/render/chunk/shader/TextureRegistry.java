package me.jellysquid.mods.sodium.client.render.chunk.shader;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.util.Identifier;

import java.util.Map;

public class TextureRegistry {
    public static final TextureRegistry INSTANCE = new TextureRegistry();

    private final Object2IntMap<Identifier> byName = new Object2IntOpenHashMap<>();

    public TextureRegistry() {
        this.byName.put(new Identifier("minecraft:block_tex"), 0);
        this.byName.put(new Identifier("minecraft:light_tex"), 2);
    }

    public int getTextureId(Identifier name) {
        return this.byName.getInt(name);
    }
}
