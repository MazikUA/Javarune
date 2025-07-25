package eu.mazikkk.javarune.asset;

import java.io.InputStream;
import java.util.Optional;

public interface AssetSource {
    Optional<InputStream> getAsset(AssetPath path);
}
