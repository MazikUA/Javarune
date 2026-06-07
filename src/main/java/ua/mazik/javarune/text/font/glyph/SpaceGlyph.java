package ua.mazik.javarune.text.font.glyph;

public record SpaceGlyph(int space) implements Glyph {
    @Override
    public void render(GlyphRenderContext context) {

    }

    @Override
    public int width() {
        return this.space;
    }
}
