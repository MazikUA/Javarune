package ua.mazik.javarune.text.font.glyph;

import ua.mazik.javarune.Javarune;

public interface Glyph {
    Glyph BROKEN = new Glyph() {
        @Override
        public void render(GlyphRenderContext context) {
            Javarune.textureLoader().get("misc/asgore").ifPresent(asgore -> {
                asgore.draw(context.x(), context.y(), 8 * context.scale(), 16 * context.scale());
            });
        }

        @Override
        public int width() {
            return 8;
        }
    };

    void render(GlyphRenderContext context);

    int width();
}
