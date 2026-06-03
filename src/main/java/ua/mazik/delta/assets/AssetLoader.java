package ua.mazik.delta.assets;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AssetLoader<T extends AutoCloseable> implements AutoCloseable {
    public final AssetSource assetSource;
    public final Map<String, T> loadedAssets;

    public AssetLoader(AssetSource assetSource) {
        this.assetSource = assetSource;
        this.loadedAssets = new HashMap<>();
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
