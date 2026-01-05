package ua.mazik.javarune.font.glyph;

import ua.mazik.delta.util.Pixel;
import ua.mazik.delta.util.TextureAtlas;
import ua.mazik.javarune.render.RenderContext;

public record SpaceGlyph(int space) implements Glyph {
    @Override
    public void draw(RenderContext renderer, TextureAtlas atlas, int x, int y, Pixel color) {
        // your drawing no long
    }

    @Override
    public int width() {
        return this.space;
    }
}
