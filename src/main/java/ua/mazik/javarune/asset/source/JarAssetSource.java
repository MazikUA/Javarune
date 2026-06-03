package ua.mazik.javarune.asset.source;

import ua.mazik.javarune.Util;
import ua.mazik.javarune.asset.AssetSource;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public record JarAssetSource(Class<?> clazz) implements AssetSource {
    @Override
    public Optional<InputStream> getAsset(String path) {
        return Optional.ofNullable(clazz.getResourceAsStream("/assets/" + path));
    }

    @Override
    public List<String> listAssets(String path, String suffix) {
        List<String> assets = new ArrayList<>();

        try {
            URL url = clazz.getResource("/assets/" + path);

            if (url != null) {
                URI uri = url.toURI();
                Path folderPath = Util.getPath(uri);

                if (folderPath == null) return assets;

                try (Stream<Path> stream = Files.walk(folderPath)) {
                    stream.filter(Files::isRegularFile)
                            .filter(filePath -> filePath.toString().endsWith(suffix))
                            .forEach(filePath -> assets.add(folderPath.relativize(filePath).toString().replace("\\", "/")));
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
