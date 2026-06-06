package ua.mazik.javarune.font;

import ua.mazik.delta.codec.Codecs;
import ua.mazik.delta.codec.ObjectCodec;
import ua.mazik.delta.sdl.texture.SDLTextureAtlas;
import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.font.glyph.Glyph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class IncludeFont extends Font {
    public static final ObjectCodec<IncludeFont> CODEC = Codecs.record(
        Codecs.STRING.list().propertyOf("fonts").getter(font -> font.fonts),
        IncludeFont::new
    );

    public final List<String> fonts;
    public final List<Font> providers;

    public IncludeFont(List<String> fonts) {
        super(FontType.COMPOSE);

        this.fonts = Collections.unmodifiableList(fonts);

        List<Font> providers = new ArrayList<>();

        for (String path : fonts) {
            Javarune.fontLoader().get(path).ifPresent(providers::add);
        }

        this.providers = Collections.unmodifiableList(providers);
    }

    @Override
    public void loadGlyphs(BiConsumer<Character, Glyph> consumer, Function<Character, Glyph> function, Supplier<SDLTextureAtlas> supplier) {
        for (Font font : this.providers) {
            font.loadGlyphs(consumer, function, supplier);
        }
    }
}
