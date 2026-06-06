package ua.mazik.javarune.font;

import ua.mazik.delta.codec.Codecs;
import ua.mazik.delta.codec.ObjectCodec;
import ua.mazik.delta.sdl.texture.SDLTextureAtlas;
import ua.mazik.javarune.font.glyph.Glyph;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ComposeFont extends Font {
    public final static ObjectCodec<ComposeFont> CODEC = Codecs.record(
        Font.CODEC.list().propertyOf("providers").getter(font -> font.providers),
        ComposeFont::new
    );

    public final List<Font> providers;

    public ComposeFont(List<Font> providers) {
        super(FontType.COMPOSE);

        this.providers = Collections.unmodifiableList(providers);
    }

    @Override
    public void loadGlyphs(BiConsumer<Character, Glyph> consumer, Function<Character, Glyph> function, Supplier<SDLTextureAtlas> supplier) {
        for (Font font : this.providers) {
            font.loadGlyphs(consumer, function, supplier);
        }
    }

    @Override
    public void close() {
        super.close();

        this.providers.forEach(Font::close);
    }
}
