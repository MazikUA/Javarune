package ua.mazik.javarune.font;

import ua.mazik.delta.codec.Codec;
import ua.mazik.delta.codec.Codecs;
import ua.mazik.delta.codec.ObjectCodec;
import ua.mazik.delta.util.StringIdentifiable;
import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.font.glyph.Glyph;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class Font implements AutoCloseable {
    public static final Codec<Font> CODEC = StringIdentifiable.createCodec(FontType::values)
        .dispatch("type", font -> font.type, FontType::codec);

    public final FontType type;
    public final Overrides overrides;

    public Font(FontType type, Overrides overrides) {
        this.type = type;
        this.overrides = overrides;
    }

    public Glyph getGlyph(Character character) {
        return this.getGlyphOptional(character).orElse(Glyph.BROKEN);
    }

    public abstract Optional<Glyph> getGlyphOptional(Character character);

    @Override
    public abstract void close();

    public enum Condition implements StringIdentifiable {
        LANGUAGES(
            Codecs.STRING.list(),
            list -> list.contains(Javarune.getCurrentLanguage()),
            list -> String.join("_", list)
        );

        public static final Codec<Condition> CODEC = StringIdentifiable.createCodec(Condition::values);
        public static final Codec<Map<Condition, Object>> MAP_CODEC = Codecs.map(CODEC, condition -> condition.codec);

        public final Codec<Object> codec;
        public final Function<Object, Boolean> fulfilled;
        public final Function<Object, String> suffixier;

        @SuppressWarnings("unchecked")
        <T> Condition(Codec<T> codec, Function<T, Boolean> fulfilled, Function<T, String> suffixier) {
            this.codec = (Codec<Object>) codec;
            this.fulfilled = (Function<Object, Boolean>) fulfilled;
            this.suffixier = (Function<Object, String>) suffixier;
        }

        @Override
        public String asString() {
            return this.name().toLowerCase();
        }
    }

    public record Overrides(int priority, Map<Condition, Object> conditions) {
        public static final Overrides DEFAULT = new Overrides(0, Map.of());

        public static final Codec<Overrides> CODEC = Codecs.record(
            Codecs.INTEGER.propertyOf("priority").getter(Overrides::priority),
            Condition.MAP_CODEC.propertyOf("conditions").getter(Overrides::conditions),
            Overrides::new
        ).toCodec();

        public static final ObjectCodec<Overrides> OBJECT_CODEC = CODEC.optional(DEFAULT).propertyOf("overrides");

        public boolean isFulfilled() {
            return this.conditions.entrySet().stream()
                .allMatch(entry -> entry.getKey().fulfilled.apply(entry.getValue()));
        }

        public String getSuffix() {
            return String.join(
                "_",
                this.conditions.entrySet().stream()
                    .map(entry -> entry.getKey().suffixier.apply(entry.getValue()))
                    .toList()
            );
        }
    }
}
