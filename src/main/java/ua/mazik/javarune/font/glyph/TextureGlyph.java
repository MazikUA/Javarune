package ua.mazik.javarune.font.glyph;

import ua.mazik.delta.sdl.texture.SDLTextureAtlas;
import ua.mazik.javarune.font.TextureFont;

import java.util.Optional;
import java.util.function.Supplier;

public record TextureGlyph(String regionName, TextureFont font, TextureFont.Rect rect) implements Glyph {
    @Override
    public void render(Supplier<SDLTextureAtlas> atlas, int x, int y) {
        SDLTextureAtlas gotAtlas = atlas.get();
        Optional<SDLTextureAtlas.Region> region = gotAtlas.findRegion(this.regionName);

        if (region.isEmpty()) {
            gotAtlas.add(this.regionName, this.font.texture.data().getRegion(this.rect.x(), this.rect.y(), this.rect.w(), this.rect.h()));

            region = gotAtlas.findRegion(this.regionName);
        }

        region.ifPresent(value -> value.draw(x, y));
    }

    @Override
    public int width() {
        return this.rect.w();
    }
}
