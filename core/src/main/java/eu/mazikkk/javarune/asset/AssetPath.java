package eu.mazikkk.javarune.asset;

import eu.mazikkk.javarune.codec.Codec;
import eu.mazikkk.javarune.codec.Codecs;

public record AssetPath(String loader, String path) {
    public static final Codec<AssetPath> CODEC = Codecs.STRING.map(AssetPath::of, AssetPath::toString);

    public static AssetPath of(String stringPath) {
        String[] parts = stringPath.split(":");

        if (parts.length == 2) {
            return new AssetPath(parts[0], parts[1]);
        } else {
            return null;
        }
    }

    public static Codec<AssetPath> createCodec(AssetLoader<?> loader) {
        return Codecs.STRING.map(path -> new AssetPath(loader.name, path), AssetPath::path);
    }

    @Override
    public String toString() {
        return loader + ":" + path;
    }
}
