package eu.mazikkk.javarune.font.provider;

import eu.mazikkk.delta.codec.Codecs;
import eu.mazikkk.delta.codec.ObjectCodec;
import eu.mazikkk.javarune.font.FontType;
import eu.mazikkk.javarune.font.glyph.Glyph;
import eu.mazikkk.javarune.font.glyph.SpaceGlyph;

import java.util.HashMap;
import java.util.Map;

public record SpaceProvider(Map<String, Integer> spaces) implements FontProvider {
    public static final ObjectCodec<SpaceProvider> CODEC = Codecs.record(
            Codecs.map(Codecs.STRING, Codecs.INT).propertyOf("spaces").getter(SpaceProvider::spaces),
            SpaceProvider::new
    );

    @Override
    public Map<Character, Glyph> getGlyphs() {
        Map<Character, Glyph> glyphs = new HashMap<>();

        for (Map.Entry<String, Integer> space : this.spaces.entrySet()) {
            char character = space.getKey().charAt(0);

            glyphs.put(character, new SpaceGlyph(space.getValue()));
        }

        return glyphs;
    }

    @Override
    public FontType getType() {
        return FontType.SPACE;
    }
}
