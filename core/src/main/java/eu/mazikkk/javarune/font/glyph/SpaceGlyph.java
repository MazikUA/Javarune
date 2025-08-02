package eu.mazikkk.javarune.font.glyph;

import eu.mazikkk.javarune.draw.DrawContext;
import eu.mazikkk.javarune.font.Font;

public record SpaceGlyph(int space) implements Glyph {
    @Override
    public void draw(DrawContext context, float x, float y, Font font) {
        // your drawing no long
    }

    @Override
    public int width() {
        return this.space;
    }
}
