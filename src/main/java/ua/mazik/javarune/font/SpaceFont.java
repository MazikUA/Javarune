package ua.mazik.javarune.font;

import ua.mazik.delta.codec.Codecs;
import ua.mazik.delta.codec.ObjectCodec;
import ua.mazik.javarune.font.glyph.Glyph;
import ua.mazik.javarune.font.glyph.SpaceGlyph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SpaceFont extends Font {
    public static final ObjectCodec<SpaceFont> CODEC = Codecs.record(
        Codecs.map(Codecs.STRING, Codecs.INTEGER).propertyOf("spaces").getter(font -> font.spaces),
        Overrides.OBJECT_CODEC.getter(font -> font.overrides),
        SpaceFont::new
    );

    public final Map<String, Integer> spaces;

    public final Map<Character, Glyph> glyphs;

    public SpaceFont(Map<String, Integer> spaces, Overrides overrides) {
        super(FontType.SPACE, overrides);

        this.spaces = Collections.unmodifiableMap(spaces);

        Map<Character, Glyph> glyphs = new HashMap<>();

        for (Map.Entry<String, Integer> space : spaces.entrySet()) {
            if (space.getKey().length() != 1) continue;

            glyphs.put(space.getKey().charAt(0), new SpaceGlyph(space.getValue()));
        }

        this.glyphs = Collections.unmodifiableMap(glyphs);
    }

    @Override
    public Optional<Glyph> getGlyphOptional(Character character) {
        return Optional.ofNullable(this.glyphs.get(character));
    }

    @Override
    public void close() {

    }
}
