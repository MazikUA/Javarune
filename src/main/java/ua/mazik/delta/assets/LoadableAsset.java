package ua.mazik.delta.assets;

import ua.mazik.delta.codec.Codec;
import ua.mazik.delta.codec.Codecs;

import java.util.Optional;
import java.util.function.Supplier;

public record LoadableAsset<T>(String path, Supplier<Optional<T>> dataSupplier) {
    public static <R> Codec<LoadableAsset<R>> codec(AssetLoader<R> loader) {
        return Codecs.STRING.map(loader::getAsAsset, LoadableAsset::path);
    }

    public T data() {
        return this.dataSupplier.get().orElseThrow();
    }
}
