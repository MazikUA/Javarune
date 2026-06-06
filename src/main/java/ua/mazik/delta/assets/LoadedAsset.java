package ua.mazik.delta.assets;

import ua.mazik.delta.codec.Codec;
import ua.mazik.delta.codec.Codecs;

public record LoadedAsset<T>(String path, T data) {
    public static <R extends AutoCloseable> Codec<LoadedAsset<R>> codec(AssetLoader<R> loader) {
        return Codecs.STRING.map(
            str -> loader.getAsAsset(str).orElseThrow(),
            asset -> asset.path
        );
    }
}
