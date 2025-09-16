package eu.mazikkk.javarune.font.glyph;

import eu.mazikkk.delta.Renderer;
import eu.mazikkk.javarune.font.Font;

public interface Glyph {
    Glyph BROKEN = new AssgoreGlyph();

    void draw(Renderer renderer, int x, int y, Font font);

    int width();
}
