package ua.mazik.javarune.font;

import ua.mazik.delta.codec.Codecs;
import ua.mazik.delta.codec.ObjectCodec;
import ua.mazik.javarune.font.glyph.Glyph;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ComposeFont extends Font {
    public final static ObjectCodec<ComposeFont> CODEC = Codecs.record(
        Font.CODEC.list().propertyOf("providers").getter(font -> font.providers),
        Overrides.OBJECT_CODEC.getter(font -> font.overrides),
        ComposeFont::new
    );

    public final List<Font> providers;

    public ComposeFont(List<Font> providers, Overrides overrides) {
        super(FontType.COMPOSE, overrides);

        providers.sort(Comparator.comparingInt((Font font) -> font.overrides.priority()).reversed());

        this.providers = Collections.unmodifiableList(providers);
    }

    @Override
    public Optional<Glyph> getGlyphOptional(Character character) {
        for (Font font : this.providers) {
            if (!font.overrides.isFulfilled()) continue;

            Optional<Glyph> glyph = font.getGlyphOptional(character);

            if (glyph.isPresent()) {
                return glyph;
            }
        }

        return Optional.empty();
    }

    @Override
    public void close() {
        this.providers.forEach(Font::close);
    }
}
