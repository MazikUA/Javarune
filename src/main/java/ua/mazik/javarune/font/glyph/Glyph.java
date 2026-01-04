package ua.mazik.javarune.font.glyph;

import ua.mazik.delta.util.TextureAtlas;
import ua.mazik.javarune.render.RenderContext;

public interface Glyph {
    Glyph BROKEN = new AssgoreGlyph();

    void draw(RenderContext renderer, TextureAtlas atlas, int x, int y);

    int width();
}
