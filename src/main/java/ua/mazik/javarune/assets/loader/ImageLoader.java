package ua.mazik.javarune.assets.loader;

import ua.mazik.delta.assets.Asset;
import ua.mazik.delta.assets.AssetSource;
import ua.mazik.delta.assets.CacheableAssetLoader;
import ua.mazik.delta.spng.SPNGImage;
import ua.mazik.delta.util.LWJGLUtil;

import java.util.Optional;

public class ImageLoader extends CacheableAssetLoader<SPNGImage> {
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
