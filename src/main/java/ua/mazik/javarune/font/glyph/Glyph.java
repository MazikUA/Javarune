package ua.mazik.javarune.font.glyph;

import ua.mazik.delta.renderer.Renderer;
import ua.mazik.javarune.font.Font;

public interface Glyph {
    Glyph BROKEN = new AssgoreGlyph();

    void draw(Renderer renderer, int x, int y, Font font);

    int width();
}
