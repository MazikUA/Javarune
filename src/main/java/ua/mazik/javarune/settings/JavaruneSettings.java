package ua.mazik.javarune.settings;

import ua.mazik.delta.codec.Codec;
import ua.mazik.delta.codec.Codecs;
import ua.mazik.delta.fs.DeltaFile;
import ua.mazik.delta.settings.DeltaSettings;
import ua.mazik.delta.util.StringIdentifiable;

import java.util.function.Function;

public class JavaruneSettings extends DeltaSettings {
    public final DeltaSettings.Field<String> locale = this.addField("locale", Codecs.STRING, "en_us");
    public final DeltaSettings.Field<GlyphWidth> dialogGlyphWidth = this.addField("glyph_width", GlyphWidth.CODEC, GlyphWidth.DYNAMIC);

    public JavaruneSettings(DeltaFile file) {
        super(file);
    }

    public enum GlyphWidth implements StringIdentifiable {
        FANCY(Function.identity()),
        DYNAMIC(width -> Math.max(width, 7)),
        FORCE_7(width -> 7);

        public static final Codec<GlyphWidth> CODEC = StringIdentifiable.createCodec(GlyphWidth::values);

        public final Function<Integer, Integer> converter;

        GlyphWidth(Function<Integer, Integer> converter) {
            this.converter = converter;
        }

        @Override
        public String asString() {
            return this.name().toLowerCase();
        }
    }
}
