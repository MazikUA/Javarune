package ua.mazik.javarune.asset;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public interface AssetSource {
    Optional<InputStream> getAsset(String path);

    List<String> listAssets(String path, String suffix);
}
