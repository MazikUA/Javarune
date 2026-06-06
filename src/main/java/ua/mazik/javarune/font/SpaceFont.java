package ua.mazik.javarune.font;

import ua.mazik.delta.codec.Codecs;
import ua.mazik.delta.codec.ObjectCodec;
import ua.mazik.delta.sdl.texture.SDLTextureAtlas;
import ua.mazik.javarune.font.glyph.Glyph;
import ua.mazik.javarune.font.glyph.SpaceGlyph;

import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SpaceFont extends Font {
    public static final ObjectCodec<SpaceFont> CODEC = Codecs.record(
        Codecs.map(Codecs.STRING, Codecs.INTEGER).propertyOf("glyphs").getter(font -> font.glyphs),
        SpaceFont::new
    );

    public final Map<String, Integer> glyphs;

    public SpaceFont(Map<String, Integer> glyphs) {
        super(FontType.SPACE);

        this.glyphs = Collections.unmodifiableMap(glyphs);
    }

    @Override
    public void loadGlyphs(BiConsumer<Character, Glyph> consumer, Function<Character, Glyph> function, Supplier<SDLTextureAtlas> supplier) {
        for (Map.Entry<String, Integer> glyph : this.glyphs.entrySet()) {
            consumer.accept(glyph.getKey().charAt(0), new SpaceGlyph(glyph.getValue()));
        }
    }
}
