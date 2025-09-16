package eu.mazikkk.javarune.asset;

import org.jspecify.annotations.NonNull;

public abstract class AssetLoader<T> implements AutoCloseable {
    public final String name;
    public final AssetManager assetManager;

    public AssetLoader(@NonNull String name, @NonNull AssetManager assetManager) {
        this.name = name;
        this.assetManager = assetManager;
    }

    public abstract void load();

    public abstract @NonNull T get(@NonNull String path);

    @Override
    public abstract void close();
}
