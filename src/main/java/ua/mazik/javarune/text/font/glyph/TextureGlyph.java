package ua.mazik.javarune.text.font.glyph;

import ua.mazik.delta.sdl.texture.SDLQuad;
import ua.mazik.delta.sdl.texture.SDLTextureAtlas;
import ua.mazik.delta.util.Pixel;
import ua.mazik.javarune.text.font.TextureFont;

import java.util.Optional;
import java.util.function.Supplier;

public record TextureGlyph(String regionName, TextureFont font, TextureFont.Rect rect) implements Glyph {
    @Override
    public void render(Supplier<SDLTextureAtlas> atlas, int x, int y, int scale, Pixel topColor, Pixel bottomColor) {
        SDLTextureAtlas gotAtlas = atlas.get();
        Optional<SDLTextureAtlas.Region> region = gotAtlas.findRegion(this.regionName);

        if (region.isEmpty()) {
            gotAtlas.add(this.regionName, this.font.texture.data().getRegion(this.rect.x(), this.rect.y(), this.rect.w(), this.rect.h()));

            region = gotAtlas.findRegion(this.regionName);
        }

        region.ifPresent(value -> {
            SDLQuad quad = new SDLQuad();

            quad.setPosition(x, y);
            quad.setSize(value.w() * scale, value.h() * scale);

            quad.topLeft.color = topColor;
            quad.topRight.color = topColor;

            quad.bottomRight.color = bottomColor;
            quad.bottomLeft.color = bottomColor;

            value.drawAsQuad(quad);
        });
    }

    @Override
    public int width() {
        return this.rect.w();
    }
}
