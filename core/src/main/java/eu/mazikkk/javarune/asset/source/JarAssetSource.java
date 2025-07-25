package eu.mazikkk.javarune.asset.source;

import eu.mazikkk.javarune.asset.AssetPath;
import eu.mazikkk.javarune.asset.AssetSource;

import java.io.InputStream;
import java.util.Optional;

public record JarAssetSource(Class<?> clazz) implements AssetSource {
    @Override
    public Optional<InputStream> getAsset(AssetPath path) {
        return Optional.ofNullable(clazz.getResourceAsStream("/assets/" + path.loader() + "/" + path.path()));
    }
}
