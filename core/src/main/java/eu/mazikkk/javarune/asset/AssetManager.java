package eu.mazikkk.javarune.asset;

import com.badlogic.gdx.utils.Disposable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AssetManager implements Disposable {
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
    public void dispose() {
        this.loaders.forEach(AssetLoader::dispose);
        this.loaders.clear();
        this.sources.clear();
    }
}
