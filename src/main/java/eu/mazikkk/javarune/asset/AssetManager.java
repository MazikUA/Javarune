package eu.mazikkk.javarune.asset;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AssetManager implements AutoCloseable {
    public final List<AssetSource> sources = new ArrayList<>();
    public final List<AssetLoader<?>> loaders = new ArrayList<>();

    public void registerLoader(AssetLoader<?> assetLoader) {
        this.loaders.add(assetLoader);
    }

    public void registerSource(AssetSource assetSource) {
        this.sources.add(assetSource);
    }

    public void load() {
        this.loaders.forEach(AssetLoader::load);
    }

    public Optional<InputStream> findAsset(String path) {
        for (AssetSource source : sources) {
            Optional<InputStream> asset = source.getAsset(path);

            if (asset.isPresent()) {
                return asset;
            }
        }

        return Optional.empty();
    }

    @Override
    public void close() {
        this.loaders.forEach(AssetLoader::close);
        this.loaders.clear();
        this.sources.clear();
    }
}
