package ua.mazik.javarune.font.glyph;

import ua.mazik.javarune.font.Font;
import ua.mazik.javarune.render.RenderContext;

public record SpaceGlyph(int space) implements Glyph {
    @Override
    public void draw(RenderContext renderer, int x, int y, Font font) {
        // your drawing no long
    }

    @Override
    public int width() {
        return this.space;
    }
}
