package eu.mazikkk.javarune.codec;

import com.google.gson.JsonObject;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface ObjectCodec<T> {
    static <T> ObjectCodec<T> of(Function<JsonObject, T> decoder, BiConsumer<T, JsonObject> encoder) {
        return new ObjectCodec<>() {
            @Override
            public T decode(JsonObject object) {
                return decoder.apply(object);
            }

            @Override
            public void encode(T value, JsonObject object) {
                encoder.accept(value, object);
            }
        };
    }

    T decode(JsonObject object);

    void encode(T value, JsonObject object);

    default Codec<T> toCodec() {
        return Codec.of(
            json -> this.decode(json.getAsJsonObject()),
            value -> {
                JsonObject object = new JsonObject();
                this.encode(value, object);
                return object;
            }
        );
    }

    default <I> RecordCodec<T, I> getter(Function<I, T> getter) {
        return new RecordCodec<>(getter, this);
    }
}
