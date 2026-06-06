package ua.mazik.javarune.font;

import ua.mazik.delta.assets.LoadedAsset;
import ua.mazik.delta.codec.Codec;
import ua.mazik.delta.codec.Codecs;
import ua.mazik.delta.codec.ObjectCodec;
import ua.mazik.delta.sdl.texture.SDLTextureAtlas;
import ua.mazik.delta.spng.SPNGImage;
import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.font.glyph.Glyph;
import ua.mazik.javarune.font.glyph.TextureGlyph;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class TextureFont extends Font {
    public static final ObjectCodec<TextureFont> CODEC = Codecs.record(
        Javarune.imageLoader().codec.propertyOf("texture").getter(font -> font.texture),
        Codecs.map(Codecs.STRING, Rect.CODEC).propertyOf("glyphs").getter(font -> {
            throw new RuntimeException();
        }),
        Overrides.CODEC.optional(null).propertyOf("overrides").getter(font -> font.overrides),
        TextureFont::new
    );

    public final LoadedAsset<SPNGImage> texture;
    public final Map<Character, Rect> glyphs;
    public final Overrides overrides;

    public TextureFont(LoadedAsset<SPNGImage> texture, Map<String, Rect> glyphs, Overrides overrides) {
        super(FontType.TEXTURE);

        this.texture = texture;

        Map<Character, Rect> temp = new HashMap<>();

        for (Map.Entry<String, Rect> glyph : glyphs.entrySet()) {
            temp.put(glyph.getKey().charAt(0), glyph.getValue());
        }

        this.glyphs = Collections.unmodifiableMap(temp);
        this.overrides = overrides;
    }

    @Override
    public void loadGlyphs(BiConsumer<Character, Glyph> consumer, Function<Character, Glyph> function, Supplier<SDLTextureAtlas> supplier) {
        for (Character character : this.glyphs.keySet()) {
            Glyph g = function.apply(character);

            if (g != null) {
                if (this.overrides != null && g instanceof TextureGlyph textureGlyph) {
                    textureGlyph.overrides().put(this, this.overrides);
                    continue;
                }
                continue;
            }

            Map<TextureFont, TextureFont.Overrides> ma = new HashMap<>();

            if (this.overrides != null) {
                ma.put(this, this.overrides);
            }

            consumer.accept(character, new TextureGlyph(supplier, ma, this, character));
        }
    }

    public record Rect(int x, int y, int w, int h) {
        public static final Codec<Rect> CODEC = Codecs.INTEGER.list().map(
            list -> {
                if (list.size() != 4) {
                    throw new RuntimeException();
                }
                return new Rect(list.get(0), list.get(1), list.get(2), list.get(3));
            },
            rect -> List.of(rect.x, rect.y, rect.w, rect.h)
        );
    }

    public record Overrides(int priority, String language) {
        public static final Codec<Overrides> CODEC = Codecs.record(
            Codecs.INTEGER.propertyOf("priority").getter(Overrides::priority),
            Codecs.STRING.propertyOf("language").getter(Overrides::language),
            Overrides::new
        ).toCodec();
    }
}
