package ua.mazik.javarune.font.glyph;

import ua.mazik.delta.renderer.Renderer;
import ua.mazik.javarune.font.Font;

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
