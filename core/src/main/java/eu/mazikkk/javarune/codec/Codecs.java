package eu.mazikkk.javarune.codec;

import com.badlogic.gdx.graphics.Pixmap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import eu.mazikkk.javarune.Javarune;
import eu.mazikkk.javarune.asset.PixmapAsset;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Codecs {
    public static final Codec<Boolean> BOOLEAN = Codec.of(JsonElement::getAsBoolean, JsonPrimitive::new);
    public static final Codec<Integer> INT = Codec.of(JsonElement::getAsInt, JsonPrimitive::new);
    public static final Codec<Float> FLOAT = Codec.of(JsonElement::getAsFloat, JsonPrimitive::new);
    public static final Codec<String> STRING = Codec.of(JsonElement::getAsString, JsonPrimitive::new);
    public static final Codec<PixmapAsset> PIXMAP = STRING.map(
        path -> {
            Optional<InputStream> asset = Javarune.assetManager.findAsset("textures/" + path + ".png");

            if (asset.isPresent()) {
                try (InputStream in = asset.get()) {
                    byte[] bytes = in.readAllBytes();

                    return new PixmapAsset(new Pixmap(bytes, 0, bytes.length), path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            throw new UnsupportedOperationException();
        },
        PixmapAsset::path
    );

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
