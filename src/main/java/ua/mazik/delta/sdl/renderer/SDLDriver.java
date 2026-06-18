package ua.mazik.delta.sdl.renderer;

import ua.mazik.delta.util.StringIdentifiable;

public enum SDLDriver implements StringIdentifiable {
    OPENGL,
    VULKAN,
    METAL;

    @Override
    public String asString() {
        return this.name().toLowerCase();
    }
}
