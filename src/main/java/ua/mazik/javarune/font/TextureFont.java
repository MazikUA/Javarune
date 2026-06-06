package ua.mazik.javarune.font;

import ua.mazik.delta.assets.LoadedAsset;
import ua.mazik.delta.codec.Codec;
import ua.mazik.delta.codec.Codecs;
import ua.mazik.delta.codec.ObjectCodec;
import ua.mazik.delta.spng.SPNGImage;
import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.font.glyph.Glyph;
import ua.mazik.javarune.font.glyph.TextureGlyph;

import java.util.*;

public class TextureFont extends Font {
    public static final ObjectCodec<TextureFont> CODEC = Codecs.record(
        Javarune.imageLoader().assetCodec.propertyOf("texture").getter(font -> font.texture),
        Codecs.map(Codecs.STRING, Rect.CODEC).propertyOf("rects").getter(font -> font.rects),
        Overrides.OBJECT_CODEC.getter(font -> font.overrides),
        TextureFont::new
    );

    public final LoadedAsset<SPNGImage> texture;
    public final Map<String, Rect> rects;

    public final Map<Character, Glyph> glyphs;

    public TextureFont(LoadedAsset<SPNGImage> texture, Map<String, Rect> rects, Overrides overrides) {
        super(FontType.TEXTURE, overrides);

        this.texture = texture;
        this.rects = rects;

        Map<Character, Glyph> temp = new HashMap<>();

        for (Map.Entry<String, Rect> rect : rects.entrySet()) {
            String regionName = "character_" + rect.getValue();
            String suffix = this.overrides.getSuffix();

            if (!suffix.isEmpty()) {
                regionName += "_" + suffix;
            }

            temp.put(rect.getKey().charAt(0), new TextureGlyph(regionName, this, rect.getValue()));
        }

        this.glyphs = Collections.unmodifiableMap(temp);
    }

    @Override
    public Optional<Glyph> getGlyphOptional(Character character) {
        return Optional.ofNullable(this.glyphs.get(character));
    }

    @Override
    public void close() {

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
