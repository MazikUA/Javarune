package ua.mazik.javarune.util;

import ua.mazik.delta.sdl.renderer.SDLRenderer;
import ua.mazik.delta.sdl.texture.SDLTextureAtlas;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AtlasManager implements AutoCloseable {
    public final Map<String, SDLTextureAtlas> atlases;
    public final SDLRenderer renderer;

    public AtlasManager(SDLRenderer renderer) {
        this.atlases = new HashMap<>();
        this.renderer = renderer;
    }

    public Optional<SDLTextureAtlas> get(String name) {
        return Optional.ofNullable(this.atlases.get(name));
    }

    public SDLTextureAtlas getOrCreate(String name, int width, int height) {
        return this.atlases.computeIfAbsent(name, str -> new SDLTextureAtlas(width, height, this.renderer));
    }

    public SDLTextureAtlas getOrCreate(String name, int size) {
        return this.getOrCreate(name, size, size);
    }

    @Override
    public void close() {
        this.atlases.values().forEach(SDLTextureAtlas::close);
    }
}
