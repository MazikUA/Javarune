package eu.mazikkk.delta.codec;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.HashMap;
import java.util.Map;

public class Codecs {
    public static final Codec<Boolean> BOOLEAN = Codec.of(JsonElement::getAsBoolean, JsonPrimitive::new);
    public static final Codec<Integer> INT = Codec.of(JsonElement::getAsInt, JsonPrimitive::new);
    public static final Codec<Float> FLOAT = Codec.of(JsonElement::getAsFloat, JsonPrimitive::new);
    public static final Codec<String> STRING = Codec.of(JsonElement::getAsString, JsonPrimitive::new);

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
}
