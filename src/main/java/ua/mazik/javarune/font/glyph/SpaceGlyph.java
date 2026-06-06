package ua.mazik.javarune.font.glyph;

public record SpaceGlyph(int space) implements Glyph {
    @Override
    public void render(int x, int y) {

    }

    @Override
    public int width() {
        return this.space;
    }
}
