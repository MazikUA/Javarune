package eu.mazikkk.javarune.font.glyph;

import eu.mazikkk.delta.Renderer;
import eu.mazikkk.javarune.font.Font;

public record SpaceGlyph(int space) implements Glyph {
    @Override
    public void draw(Renderer renderer, int x, int y, Font font) {
        // your drawing no long
    }

    @Override
    public int width() {
        return this.space;
    }
}
