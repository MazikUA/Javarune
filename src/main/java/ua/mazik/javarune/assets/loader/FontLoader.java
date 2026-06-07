package ua.mazik.javarune.assets.loader;

import ua.mazik.delta.assets.AssetSource;
import ua.mazik.delta.assets.CacheableAssetLoader;
import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.text.font.Font;

import java.util.Optional;

public class FontLoader extends CacheableAssetLoader<Font> {
    public FontLoader(AssetSource assetSource) {
        super(assetSource);
    }

    @Override
    public Optional<Font> load(String path) {
        return Javarune.jsonLoader().load("fonts/" + path).map(Font.CODEC::decode);
    }
}
