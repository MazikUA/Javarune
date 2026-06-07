package ua.mazik.javarune.text.font.glyph;

import ua.mazik.delta.sdl.texture.SDLQuad;
import ua.mazik.delta.sdl.texture.SDLTextureAtlas;
import ua.mazik.javarune.text.font.TextureFont;

import java.util.Optional;

public record TextureGlyph(String regionName, TextureFont font, TextureFont.Rect rect) implements Glyph {
    @Override
    public void render(GlyphRenderContext context) {
        SDLTextureAtlas gotAtlas = context.atlas().get();
        Optional<SDLTextureAtlas.Region> region = gotAtlas.findRegion(this.regionName);

        if (region.isEmpty()) {
            gotAtlas.add(this.regionName, this.font.texture.data().getRegion(this.rect.x(), this.rect.y(), this.rect.w(), this.rect.h()));

            region = gotAtlas.findRegion(this.regionName);
        }

        region.ifPresent(value -> {
            SDLQuad quad = new SDLQuad();

            quad.setPosition(context.x(), context.y());
            quad.setSize(value.w() * context.scale(), value.h() * context.scale());

            quad.topLeft.color = context.topColor();
            quad.topRight.color = context.topColor();

            quad.bottomRight.color = context.bottomColor();
            quad.bottomLeft.color = context.bottomColor();

            value.drawAsQuad(quad);
        });
    }

    @Override
    public int width() {
        return this.rect.w();
    }
}
