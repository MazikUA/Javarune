package ua.mazik.javarune.asset;

import org.jspecify.annotations.NonNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class AssetManager implements AutoCloseable {
    public final List<AssetSource> sources = new ArrayList<>();
    public final List<AssetLoader<?>> loaders = new ArrayList<>();

    public <T extends AssetLoader<?>> @NonNull T registerLoader(Function<AssetManager, T> assetLoader) {
        T loader = assetLoader.apply(this);
        this.loaders.add(loader);
        return loader;
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
