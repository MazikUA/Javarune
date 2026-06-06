package ua.mazik.javarune.assets.loader;

import ua.mazik.delta.assets.Asset;
import ua.mazik.delta.assets.AssetLoader;
import ua.mazik.delta.assets.AssetSource;
import ua.mazik.delta.assets.LoadedAsset;
import ua.mazik.delta.codec.Codec;
import ua.mazik.delta.spng.SPNGImage;
import ua.mazik.delta.util.LWJGLUtil;

import java.util.Optional;

public class ImageLoader extends AssetLoader<SPNGImage> {
    public Codec<LoadedAsset<SPNGImage>> codec = LoadedAsset.codec(this);

    public ImageLoader(AssetSource assetSource) {
        super(assetSource);
    }

    @Override
    public Optional<SPNGImage> load(String path) {
        Optional<Asset> asset = this.assetSource.getAsset("textures/" + path + ".png");

        if (asset.isPresent()) {
            byte[] bytes = asset.get().getBytes();

            SPNGImage png = new SPNGImage(LWJGLUtil.createBuffer(bytes));

            return Optional.of(png);
        }

        return Optional.empty();
    }
}
