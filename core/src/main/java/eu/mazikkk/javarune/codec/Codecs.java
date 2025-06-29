package eu.mazikkk.javarune.codec;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.HashMap;
import java.util.Map;

public class Codecs {
    public static final Codec<Boolean> BOOLEAN = Codec.of(JsonElement::getAsBoolean, JsonPrimitive::new);
    public static final Codec<Integer> INT = Codec.of(JsonElement::getAsInt, JsonPrimitive::new);
    public static final Codec<String> STRING = Codec.of(JsonElement::getAsString, JsonPrimitive::new);

    public static <V> Codec<Map<String, V>> map(Codec<V> valueCodec) {
        return Codec.of(
            json -> {
                Map<String, V> map = new HashMap<>();

                for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().asMap().entrySet()) {
                    map.put(entry.getKey(), valueCodec.decode(entry.getValue()));
                }

                return map;
            },
            map -> {
                JsonObject object = new JsonObject();

                for (Map.Entry<String, V> entry : map.entrySet()) {
                    object.add(entry.getKey(), valueCodec.encode(entry.getValue()));
                }

                return object;
            }
        );
    }
}
