package ua.mazik.delta.util;

import ua.mazik.delta.codec.Codec;
import ua.mazik.delta.codec.Codecs;

import java.util.Arrays;
import java.util.function.Supplier;

public interface StringIdentifiable {
    static <T extends StringIdentifiable> Codec<T> createCodec(Supplier<T[]> values) {
        return Codecs.STRING.map(
                name -> Arrays.stream(values.get()).filter(obj -> obj.asString().equals(name)).findFirst().orElse(null),
                value -> Arrays.stream(values.get()).filter(obj -> obj == value).findFirst().orElse(null).asString()
        );
    }

    String asString();
}
