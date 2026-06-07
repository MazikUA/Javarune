package ua.mazik.javarune.assets.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ua.mazik.delta.assets.Asset;
import ua.mazik.delta.assets.AssetLoader;
import ua.mazik.delta.assets.AssetSource;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class JsonLoader extends AssetLoader<JsonElement> {
    public JsonLoader(AssetSource assetSource) {
        super(assetSource);
    }

    @Override
    public Optional<JsonElement> load(String path) {
        Optional<Asset> asset = this.assetSource.getAsset(path + ".json");

        if (asset.isPresent()) {
            byte[] bytes = asset.get().getBytes();

            JsonElement json = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8));

            return Optional.of(json);
        }

        return Optional.empty();
    }
}
