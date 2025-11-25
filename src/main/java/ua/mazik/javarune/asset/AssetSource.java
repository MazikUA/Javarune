package ua.mazik.javarune.asset;

import java.io.InputStream;
import java.util.Optional;

public interface AssetSource {
    Optional<InputStream> getAsset(String path);
}
