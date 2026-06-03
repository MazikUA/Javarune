package ua.mazik.javarune.font;

import ua.mazik.delta.codec.Codec;
import ua.mazik.delta.codec.Codecs;
import ua.mazik.delta.util.TextureAtlas;
import ua.mazik.javarune.font.glyph.Glyph;
import ua.mazik.javarune.font.provider.FontProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Font implements AutoCloseable {
    public static final Codec<Font> CODEC = Codecs.record(
            FontProvider.CODEC.list().propertyOf("providers").getter(font -> font.providers),
            Font::new
    ).toCodec();

    public final List<FontProvider> providers;
    public final Map<Character, Glyph> glyphs = new HashMap<>();
    public final TextureAtlas atlas = new TextureAtlas(2048, 2048);

    public Font(List<FontProvider> providers) {
        this.providers = providers;

        for (FontProvider provider : providers) {
            glyphs.putAll(provider.getGlyphs());
        }
    }

    @Override
    public void close() {
        this.atlas.close();
        this.providers.clear();
    }
}
