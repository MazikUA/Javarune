package ua.mazik.javarune.font.glyph;

import ua.mazik.delta.sdl.texture.SDLTextureAtlas;
import ua.mazik.javarune.font.TextureFont;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public record TextureGlyph(Supplier<SDLTextureAtlas> atlas, Map<TextureFont, TextureFont.Overrides> overrides, TextureFont defaultFont, Character character) implements Glyph {
    @Override
    public void render(int x, int y) {
        TextureFont font = this.winningFont();

        String regionName = "character_" + this.character;

        if (font.overrides != null) {
            regionName += "_" + font.overrides.language();
        }

        SDLTextureAtlas atlas = this.atlas.get();
        Optional<SDLTextureAtlas.Region> region = atlas.findRegion(regionName);

        if (region.isEmpty()) {
            TextureFont.Rect rect = font.glyphs.get(this.character);

            atlas.add(regionName, font.texture.data().getRegion(rect.x(), rect.y(), rect.w(), rect.h()));

            region = atlas.findRegion(regionName);
        }

        region.ifPresent(value -> value.draw(x, y));
    }

    @Override
    public int width() {
        return this.winningFont().glyphs.get(this.character).w();
    }

    private TextureFont winningFont() {
        Map<TextureFont, TextureFont.Overrides> map = this.overrides.entrySet()
            .stream()
            .sorted(Comparator.comparingInt((Map.Entry<TextureFont, TextureFont.Overrides> e) -> e.getValue().priority()).reversed())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (a, b) -> a,
                LinkedHashMap::new
            ));

        for (Map.Entry<TextureFont, TextureFont.Overrides> a : map.entrySet()) {
            if (a.getValue().language().equals("en_us")) {
                return a.getKey();
            }
        }

        return this.defaultFont;
    }
}
