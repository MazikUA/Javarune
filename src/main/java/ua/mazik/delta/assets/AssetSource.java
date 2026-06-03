package ua.mazik.delta.assets;

import java.util.List;
import java.util.Optional;

public interface AssetSource {
    Optional<Asset> getAsset(String path);

    List<Asset> listAssets(String path, String suffix);
}
