package eu.mazikkk.javarune.font;

import eu.mazikkk.delta.Renderer;
import eu.mazikkk.delta.codec.Codec;
import eu.mazikkk.delta.codec.RecordCodec;
import eu.mazikkk.delta.util.TextureAtlas;
import eu.mazikkk.javarune.font.glyph.Glyph;
import eu.mazikkk.javarune.font.provider.FontProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Font implements AutoCloseable {
    public static final int LINE_HEIGHT = 16;
    public static final Codec<Font> CODEC = RecordCodec.create(
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

    public void draw(Renderer renderer, String text, int x, int y) {
        for (char character : text.toCharArray()) {
            Glyph glyph = this.glyphs.getOrDefault(character, Glyph.BROKEN);

            glyph.draw(renderer, x, y, this);

            x += glyph.width();
        }
    }

    @Override
    public void close() {
        this.atlas.close();
        this.providers.clear();
    }
}
