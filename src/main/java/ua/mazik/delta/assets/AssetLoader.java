package ua.mazik.delta.assets;

import ua.mazik.delta.codec.Codec;

import java.util.Optional;

public abstract class AssetLoader<T> {
    public final AssetSource assetSource;
    public final Codec<LoadableAsset<T>> assetCodec;

    public AssetLoader(AssetSource assetSource) {
        this.assetSource = assetSource;
        this.assetCodec = LoadableAsset.codec(this);
    }

    public abstract Optional<T> load(String path);

    public Optional<T> get(String path) {
        return this.load(path);
    }

    public LoadableAsset<T> getAsAsset(String path) {
        return new LoadableAsset<>(path, () -> this.get(path));
    }
}
