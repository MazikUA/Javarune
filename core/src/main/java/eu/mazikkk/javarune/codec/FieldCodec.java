package eu.mazikkk.javarune.codec;

import com.google.gson.JsonObject;

public record FieldCodec<T>(String name, Codec<T> codec) implements ObjectCodec<T> {
    @Override
    public T decode(JsonObject object) {
        return codec.decode(object.get(name));
    }

    @Override
    public void encode(T value, JsonObject object) {
        object.add(name, codec.encode(value));
    }
}
