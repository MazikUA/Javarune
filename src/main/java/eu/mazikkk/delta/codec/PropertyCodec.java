package eu.mazikkk.delta.codec;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public record PropertyCodec<T>(String property, Codec<T> codec) implements ObjectCodec<T> {
    @Override
    public T decode(JsonObject object) {
        return codec.decode(object.get(property));
    }

    @Override
    public void encode(T value, JsonObject object) {
        JsonElement result = codec.encode(value);

        if (!result.isJsonNull()) {
            object.add(property, result);
        }
    }
}
