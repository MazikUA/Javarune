package ua.mazik.javarune.font.glyph;

import ua.mazik.delta.sdl.texture.SDLTextureAtlas;

import java.util.function.Supplier;

public record SpaceGlyph(int space) implements Glyph {
    @Override
    public void render(Supplier<SDLTextureAtlas> atlas, int x, int y) {

    }

    @Override
    public int width() {
        return this.space;
    }
}
