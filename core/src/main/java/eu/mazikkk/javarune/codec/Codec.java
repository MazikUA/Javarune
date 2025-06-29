package eu.mazikkk.javarune.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface Codec<T> {
    static <T> Codec<T> of(Function<JsonElement, T> decoder, Function<T, JsonElement> encoder) {
        return new Codec<>() {
            @Override
            public T decode(JsonElement json) {
                return decoder.apply(json);
            }

            @Override
            public JsonElement encode(T value) {
                return encoder.apply(value);
            }
        };
    }

    T decode(JsonElement json);

    JsonElement encode(T value);

    default Codec<Optional<T>> optional() {
        return of(
            json -> json == null || json.isJsonNull() ? Optional.empty() : Optional.of(this.decode(json)),
            optional -> optional.map(this::encode).orElse(JsonNull.INSTANCE)
        );
    }

    default Codec<List<T>> list() {
        return of(
            json -> {
                ArrayList<T> list = new ArrayList<>();

                for (JsonElement element : json.getAsJsonArray()) {
                    list.add(this.decode(element));
                }

                return list;
            },
            list -> {
                JsonArray array = new JsonArray();

                for (T value : list) {
                    array.add(this.encode(value));
                }

                return array;
            }
        );
    }

    default FieldCodec<T> fieldOf(String name) {
        return new FieldCodec<>(name, this);
    }

    default <O> Codec<O> map(Function<T, O> decoder, Function<O, T> encoder) {
        return of(
            json -> decoder.apply(this.decode(json)),
            value -> this.encode(encoder.apply(value))
        );
    }
}
