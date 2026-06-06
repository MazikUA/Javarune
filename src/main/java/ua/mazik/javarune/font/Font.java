package ua.mazik.javarune.font;

import ua.mazik.delta.codec.Codec;
import ua.mazik.delta.sdl.texture.SDLTextureAtlas;
import ua.mazik.delta.util.StringIdentifiable;
import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.font.glyph.Glyph;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Font implements AutoCloseable {
    public static final Codec<Font> CODEC = StringIdentifiable.createCodec(FontType::values)
        .dispatch("type", Font::type, FontType::codec);

    private final FontType type;
    private final Map<Character, Glyph> glyphs;

    private SDLTextureAtlas atlas;

    public Font(FontType type) {
        this.type = type;
        this.glyphs = new HashMap<>();
    }

    private FontType type() {
        return this.type;
    }

    public SDLTextureAtlas getAtlas() {
        if (this.atlas == null) {
            this.atlas = new SDLTextureAtlas(2048, 2048, Javarune.renderer());
        }
        return this.atlas;
    }

    public void loadGlyphs() {
        this.loadGlyphs(this.glyphs::put, this.glyphs::get, this::getAtlas);
    }

    public abstract void loadGlyphs(BiConsumer<Character, Glyph> consumer, Function<Character, Glyph> function, Supplier<SDLTextureAtlas> supplier);

    public Optional<Glyph> getGlyph(char character) {
        return Optional.ofNullable(this.glyphs.get(character));
    }

    @Override
    public void close() {
        this.glyphs.clear();

        if (this.atlas != null) {
            this.atlas.close();
        }
    }
}
