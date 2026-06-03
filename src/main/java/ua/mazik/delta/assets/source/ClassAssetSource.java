package ua.mazik.delta.assets.source;

import ua.mazik.delta.assets.Asset;
import ua.mazik.delta.assets.AssetSource;
import ua.mazik.delta.util.DeltaUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public record ClassAssetSource(Class<?> clazz) implements AssetSource {
    @Override
    public Optional<Asset> getAsset(String path) {
        if (clazz.getResource("/assets/" + path) == null) return Optional.empty();

        Asset asset = () -> {
            try {
                InputStream stream = clazz.getResourceAsStream("/assets/" + path);

                if (stream != null) {
                    byte[] bytes = stream.readAllBytes();

                    stream.close();

                    return bytes;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new byte[0];
        };

        return Optional.of(asset);
    }

    @Override
    public List<Asset> listAssets(String path, String suffix) {
        List<Asset> assets = new ArrayList<>();

        try {
            URL url = clazz.getResource("/assets/" + path);

            if (url != null) {
                URI uri = url.toURI();
                Path folderPath = DeltaUtil.getPath(uri);

                if (folderPath == null) return assets;

                try (Stream<Path> stream = Files.walk(folderPath)) {
                    stream.filter(Files::isRegularFile)
                        .filter(filePath -> filePath.toString().endsWith(suffix))
                        .forEach(filePath -> {
                            this.getAsset(path + "/" + folderPath.relativize(filePath).toString().replace("\\", "/"))
                                .ifPresent(assets::add);
                        });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return assets;
    }
}
