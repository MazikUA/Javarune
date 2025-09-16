package eu.mazikkk.javarune.asset;

import eu.mazikkk.delta.codec.Codec;
import eu.mazikkk.delta.codec.Codecs;
import eu.mazikkk.delta.util.Pixmap;
import eu.mazikkk.javarune.Javarune;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public record PixmapAsset(Pixmap pixmap, String path) implements AutoCloseable {
    public static final Codec<PixmapAsset> CODEC = Codecs.STRING.map(
            path -> {
                Optional<InputStream> asset = Javarune.getInstance().assetManager.findAsset("textures/" + path + ".png");

                if (asset.isPresent()) {
                    try (InputStream in = asset.get()) {
                        byte[] bytes = in.readAllBytes();

                        return new PixmapAsset(Pixmap.fromImageBytes(bytes), path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                throw new UnsupportedOperationException();
            },
            PixmapAsset::path
    );

    @Override
    public void close() {
        this.pixmap.close();
    }
}
