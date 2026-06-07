package ua.mazik.delta.assets;

import ua.mazik.delta.codec.Codec;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AssetLoader<T extends AutoCloseable> implements AutoCloseable {
    public final AssetSource assetSource;
    public final Map<String, T> loadedAssets;
    public final Codec<LoadableAsset<T>> assetCodec;

    public AssetLoader(AssetSource assetSource) {
        this.assetSource = assetSource;
        this.loadedAssets = new HashMap<>();
        this.assetCodec = LoadableAsset.codec(this);
    }

    public abstract Optional<T> load(String path);

    public Optional<T> get(String path) {
        T loaded = this.loadedAssets.get(path);

        if (loaded == null) {
            Optional<T> freshLoaded = this.load(path);

            freshLoaded.ifPresent((asset) -> this.loadedAssets.put(path, asset));

            return freshLoaded;
        }
        return Optional.of(loaded);
    }

    public LoadableAsset<T> getAsAsset(String path) {
        return new LoadableAsset<>(path, () -> this.get(path));
    }

    @Override
    public void close() {
        if (this.loadedAssets.isEmpty()) return;

        this.loadedAssets.values().forEach(asset -> {
            try {
                asset.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        this.loadedAssets.clear();
    }
}
