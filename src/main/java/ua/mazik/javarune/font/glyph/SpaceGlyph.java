package ua.mazik.javarune.font.glyph;

import ua.mazik.delta.util.TextureAtlas;
import ua.mazik.javarune.font.TextColor;
import ua.mazik.javarune.render.RenderContext;

public record SpaceGlyph(int space) implements Glyph {
    @Override
    public void draw(RenderContext renderer, TextureAtlas atlas, int x, int y, int scale, TextColor color) {
        // your drawing no long
    }

    @Override
    public int width() {
        return this.space;
    }
}
