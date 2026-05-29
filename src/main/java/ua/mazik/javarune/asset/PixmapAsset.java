package ua.mazik.javarune.asset;

import ua.mazik.delta.codec.Codec;
import ua.mazik.delta.codec.Codecs;
import ua.mazik.delta.util.Pixmap;
import ua.mazik.javarune.Javarune;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public record PixmapAsset(Pixmap pixmap, String path) {
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
}
