package ua.mazik.delta.codec;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import ua.mazik.delta.util.function.PentaFunction;
import ua.mazik.delta.util.function.QuadFunction;
import ua.mazik.delta.util.function.TriFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class Codecs {
    public static final Codec<Boolean> BOOLEAN = Codec.of(JsonElement::getAsBoolean, JsonPrimitive::new);
    public static final Codec<Integer> INTEGER = Codec.of(JsonElement::getAsInt, JsonPrimitive::new);
    public static final Codec<Float> FLOAT = Codec.of(JsonElement::getAsFloat, JsonPrimitive::new);
    public static final Codec<String> STRING = Codec.of(JsonElement::getAsString, JsonPrimitive::new);

    private Codecs() {
    }

    public static <K, V> Codec<Map<K, V>> map(Codec<K> keyCodec, Codec<V> valueCodec) {
        return Codec.of(
            json -> {
                Map<K, V> map = new HashMap<>();

                for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().asMap().entrySet()) {
                    map.put(keyCodec.decode(new JsonPrimitive(entry.getKey())), valueCodec.decode(entry.getValue()));
                }

                return map;
            },
            map -> {
                JsonObject object = new JsonObject();

                for (Map.Entry<K, V> entry : map.entrySet()) {
                    object.add(keyCodec.encode(entry.getKey()).getAsString(), valueCodec.encode(entry.getValue()));
                }

                return object;
            }
        );
    }

    public static <T1, R> ObjectCodec<R> record(GetterCodec<T1, R> t1, Function<T1, ? extends R> constructor) {
        return ObjectCodec.of(
            object -> constructor.apply(t1.objectCodec().decode(object)),
            (value, object) -> t1.objectCodec().encode(t1.getter().apply(value), object)
        );
    }

    public static <T1, T2, R> ObjectCodec<R> record(GetterCodec<T1, R> t1, GetterCodec<T2, R> t2, BiFunction<T1, T2, ? extends R> constructor) {
        return ObjectCodec.of(
            object -> constructor.apply(t1.objectCodec().decode(object), t2.objectCodec().decode(object)),
            (value, object) -> {
                t1.objectCodec().encode(t1.getter().apply(value), object);
                t2.objectCodec().encode(t2.getter().apply(value), object);
            }
        );
    }

    public static <T1, T2, T3, R> ObjectCodec<R> record(GetterCodec<T1, R> t1, GetterCodec<T2, R> t2, GetterCodec<T3, R> t3, TriFunction<T1, T2, T3, ? extends R> constructor) {
        return ObjectCodec.of(
            object -> constructor.apply(
                t1.objectCodec().decode(object),
                t2.objectCodec().decode(object),
                t3.objectCodec().decode(object)
            ),
            (value, object) -> {
                t1.objectCodec().encode(t1.getter().apply(value), object);
                t2.objectCodec().encode(t2.getter().apply(value), object);
                t3.objectCodec().encode(t3.getter().apply(value), object);
            }
        );
    }

    public static <T1, T2, T3, T4, R> ObjectCodec<R> record(GetterCodec<T1, R> t1, GetterCodec<T2, R> t2, GetterCodec<T3, R> t3, GetterCodec<T4, R> t4, QuadFunction<T1, T2, T3, T4, ? extends R> constructor) {
        return ObjectCodec.of(
            object -> constructor.apply(
                t1.objectCodec().decode(object),
                t2.objectCodec().decode(object),
                t3.objectCodec().decode(object),
                t4.objectCodec().decode(object)
            ),
            (value, object) -> {
                t1.objectCodec().encode(t1.getter().apply(value), object);
                t2.objectCodec().encode(t2.getter().apply(value), object);
                t3.objectCodec().encode(t3.getter().apply(value), object);
                t4.objectCodec().encode(t4.getter().apply(value), object);
            }
        );
    }

    public static <T1, T2, T3, T4, T5, R> ObjectCodec<R> record(GetterCodec<T1, R> t1, GetterCodec<T2, R> t2, GetterCodec<T3, R> t3, GetterCodec<T4, R> t4, GetterCodec<T5, R> t5, PentaFunction<T1, T2, T3, T4, T5, ? extends R> constructor) {
        return ObjectCodec.of(
            object -> constructor.apply(
                t1.objectCodec().decode(object),
                t2.objectCodec().decode(object),
                t3.objectCodec().decode(object),
                t4.objectCodec().decode(object),
                t5.objectCodec().decode(object)
            ),
            (value, object) -> {
                t1.objectCodec().encode(t1.getter().apply(value), object);
                t2.objectCodec().encode(t2.getter().apply(value), object);
                t3.objectCodec().encode(t3.getter().apply(value), object);
                t4.objectCodec().encode(t4.getter().apply(value), object);
                t5.objectCodec().encode(t5.getter().apply(value), object);
            }
        );
    }
}
