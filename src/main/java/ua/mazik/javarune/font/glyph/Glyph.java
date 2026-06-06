package ua.mazik.javarune.font.glyph;

import ua.mazik.delta.sdl.texture.SDLTextureAtlas;
import ua.mazik.javarune.Javarune;

import java.util.function.Supplier;

public interface Glyph {
    Glyph BROKEN = new Glyph() {
        @Override
        public void render(Supplier<SDLTextureAtlas> atlas, int x, int y) {
            Javarune.textureLoader().get("misc/asgore").ifPresent(asgore -> {
                asgore.draw(x, y, 8, 16);
            });
        }

        @Override
        public int width() {
            return 8;
        }
    };

    void render(Supplier<SDLTextureAtlas> atlas, int x, int y);

    int width();
}
