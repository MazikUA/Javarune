package ua.mazik.javarune.text.font;

import ua.mazik.delta.assets.LoadableAsset;
import ua.mazik.delta.codec.Codec;
import ua.mazik.delta.codec.Codecs;
import ua.mazik.delta.codec.ObjectCodec;
import ua.mazik.delta.spng.SPNGImage;
import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.text.font.glyph.Glyph;
import ua.mazik.javarune.text.font.glyph.TextureGlyph;

import java.util.*;

public class TextureFont extends Font {
    public static final ObjectCodec<TextureFont> CODEC = Codecs.record(
        Javarune.imageLoader().assetCodec.propertyOf("texture").getter(font -> font.texture),
        Codecs.map(Codecs.CHAR, Rect.CODEC).propertyOf("rects").getter(font -> font.rects),
        Overrides.OBJECT_CODEC.getter(font -> font.overrides),
        TextureFont::new
    );

    public final LoadableAsset<SPNGImage> texture;
    public final Map<Character, Rect> rects;

    public final Map<Character, Glyph> glyphs;

    public TextureFont(LoadableAsset<SPNGImage> texture, Map<Character, Rect> rects, Overrides overrides) {
        super(FontType.TEXTURE, overrides);

        this.texture = texture;
        this.rects = Collections.unmodifiableMap(rects);

        Map<Character, Glyph> temp = new HashMap<>();

        for (Map.Entry<Character, Rect> rect : rects.entrySet()) {
            String regionName = "character_" + rect.getKey();
            String suffix = this.overrides.getSuffix();

            if (!suffix.isEmpty()) {
                regionName += "_" + suffix;
            }

            temp.put(rect.getKey(), new TextureGlyph(regionName, this, rect.getValue()));
        }

        this.glyphs = Collections.unmodifiableMap(temp);
    }

    @Override
    public Optional<Glyph> getGlyph(char character) {
        return Optional.ofNullable(this.glyphs.get(character));
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
}
