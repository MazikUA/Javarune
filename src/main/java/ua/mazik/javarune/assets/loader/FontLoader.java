package ua.mazik.javarune.assets.loader;

import com.google.gson.JsonParser;
import ua.mazik.delta.assets.Asset;
import ua.mazik.delta.assets.AssetLoader;
import ua.mazik.delta.assets.AssetSource;
import ua.mazik.javarune.text.font.Font;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class FontLoader extends AssetLoader<Font> {
    public FontLoader(AssetSource assetSource) {
        super(assetSource);
    }

    @Override
    public Optional<Font> load(String path) {
        Optional<Asset> asset = this.assetSource.getAsset("fonts/" + path + ".json");

        if (asset.isPresent()) {
            byte[] bytes = asset.get().getBytes();

            Font font = Font.CODEC.decode(JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)));

            return Optional.of(font);
        }

        return Optional.empty();
    }
}
