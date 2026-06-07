package ua.mazik.delta.assets;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class CacheableAssetLoader<T> extends AssetLoader<T> implements AutoCloseable {
    public final Map<String, T> loadedAssets;

    public CacheableAssetLoader(AssetSource assetSource) {
        super(assetSource);

        this.loadedAssets = new HashMap<>();
    }

    @Override
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

        for (T asset : this.loadedAssets.values()) {
            if (asset instanceof AutoCloseable closeable) {
                try {
                    closeable.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }

        this.loadedAssets.clear();
    }
}
