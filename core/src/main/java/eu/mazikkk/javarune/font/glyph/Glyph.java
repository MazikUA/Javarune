package eu.mazikkk.javarune.font.glyph;

import eu.mazikkk.javarune.draw.DrawContext;
import eu.mazikkk.javarune.font.Font;

public interface Glyph {
    Glyph BROKEN = new AssgoreGlyph();

    void draw(DrawContext context, float x, float y, Font font);

    int width();
}
