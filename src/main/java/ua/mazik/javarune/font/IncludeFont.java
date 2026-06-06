package ua.mazik.javarune.font;

import ua.mazik.delta.assets.LoadedAsset;
import ua.mazik.delta.codec.Codecs;
import ua.mazik.delta.codec.ObjectCodec;
import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.font.glyph.Glyph;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class IncludeFont extends Font {
    public static final ObjectCodec<IncludeFont> CODEC = Codecs.record(
        Javarune.fontLoader().assetCodec.list().propertyOf("fonts").getter(font -> font.fonts),
        Overrides.OBJECT_CODEC.getter(font -> font.overrides),
        IncludeFont::new
    );
    public final List<LoadedAsset<Font>> fonts;

    public IncludeFont(List<LoadedAsset<Font>> fonts, Overrides overrides) {
        super(FontType.COMPOSE, overrides);

        fonts.sort(Comparator.comparingInt((LoadedAsset<Font> font) -> font.data().overrides.priority()).reversed());

        this.fonts = Collections.unmodifiableList(fonts);
    }

    @Override
    public Optional<Glyph> getGlyphOptional(Character character) {
        for (LoadedAsset<Font> font : this.fonts) {
            if (!font.data().overrides.isFulfilled()) continue;

            Optional<Glyph> glyph = font.data().getGlyphOptional(character);

            if (glyph.isPresent()) {
                return glyph;
            }
        }

        return Optional.empty();
    }

    @Override
    public void close() {
        for (LoadedAsset<Font> asset : this.fonts) {
            asset.data().close();
        }
    }
}
