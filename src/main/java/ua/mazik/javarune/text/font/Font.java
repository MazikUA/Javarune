package ua.mazik.javarune.text.font;

import ua.mazik.delta.codec.Codec;
import ua.mazik.delta.codec.Codecs;
import ua.mazik.delta.codec.ObjectCodec;
import ua.mazik.delta.util.StringIdentifiable;
import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.text.font.glyph.Glyph;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Font {
    public static final Codec<Font> CODEC = StringIdentifiable.createCodec(FontType::values)
        .dispatch("type", font -> font.type, FontType::codec);

    public final FontType type;
    public final Overrides overrides;

    public Font(FontType type, Overrides overrides) {
        this.type = type;
        this.overrides = overrides;
    }

    public abstract Optional<Glyph> getGlyph(char character, Map<Condition, Object> overrides);

    public enum Condition implements StringIdentifiable {
        LANGUAGES(
            Codecs.STRING.list(),
            (required, actual) -> {
                List<String> requiredList = cast(required);
                List<String> actualList = cast(actual);

                return !Collections.disjoint(requiredList, actualList);
            },
            list -> String.join("_", list),
            () -> List.of(Javarune.SETTINGS.language.get())
        );

        public static final Codec<Condition> CODEC = StringIdentifiable.createCodec(Condition::values);
        public static final Codec<Map<Condition, Object>> MAP_CODEC = Codecs.map(CODEC, condition -> condition.codec);

        public final Codec<Object> codec;
        public final BiPredicate<Object, Object> fulfilled;
        public final Function<Object, String> suffixier;
        public final Supplier<Object> currentValue;

        @SuppressWarnings("unchecked")
        <T> Condition(Codec<T> codec, BiPredicate<T, T> fulfilled, Function<T, String> suffixier, Supplier<T> currentValue) {
            this.codec = (Codec<Object>) codec;
            this.fulfilled = (BiPredicate<Object, Object>) fulfilled;
            this.suffixier = (Function<Object, String>) suffixier;
            this.currentValue = (Supplier<Object>) currentValue;
        }

        @SuppressWarnings("unchecked")
        private static <T> T cast(Object value) {
            return (T) value;
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

        public boolean isFulfilled(Map<Condition, Object> overrides) {
            return this.conditions.entrySet().stream().allMatch(entry -> {
                Condition condition = entry.getKey();

                Object required = entry.getValue();
                Object actual = overrides.getOrDefault(condition, condition.currentValue.get());

                return condition.fulfilled.test(required, actual);
            });
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
