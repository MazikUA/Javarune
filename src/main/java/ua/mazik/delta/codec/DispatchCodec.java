package ua.mazik.delta.codec;

import com.google.gson.JsonObject;

import java.util.function.Function;

@SuppressWarnings("unchecked")
public record DispatchCodec<T, U>(String key, Codec<U> keyCodec, Function<T, U> typeGetter, Function<U, ObjectCodec<? extends T>> codecGetter) implements ObjectCodec<T> {
    @Override
    public T decode(JsonObject object) {
        return codecGetter.apply(keyCodec.decode(object.get(key))).decode(object);
    }

    @Override
    public void encode(T value, JsonObject object) {
        U result = typeGetter.apply(value);

        object.add(key, keyCodec.encode(result));
        ((ObjectCodec<T>) codecGetter.apply(result)).encode(value, object);
    }
}
