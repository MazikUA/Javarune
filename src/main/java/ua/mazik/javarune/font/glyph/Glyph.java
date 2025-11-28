package ua.mazik.javarune.font.glyph;

import ua.mazik.javarune.font.Font;
import ua.mazik.javarune.render.RenderContext;

public interface Glyph {
    Glyph BROKEN = new AssgoreGlyph();

    void draw(RenderContext renderer, int x, int y, Font font);

    int width();
}
