package ua.mazik.javarune.text.font.glyph;

import ua.mazik.delta.sdl.texture.SDLTextureAtlas;
import ua.mazik.delta.util.Pixel;

import java.util.function.Supplier;

public record SpaceGlyph(int space) implements Glyph {
    @Override
    public void render(Supplier<SDLTextureAtlas> atlas, int x, int y, int scale, Pixel topColor, Pixel bottomColor) {

    }

    @Override
    public int width() {
        return this.space;
    }
}
