package eu.mazikkk.javarune.asset;

import com.badlogic.gdx.utils.Disposable;

public abstract class AssetLoader<T> implements Disposable {
    public final String name;
    public final AssetManager assetManager;

    public AssetLoader(String name, AssetManager assetManager) {
        this.name = name;
        this.assetManager = assetManager;
    }

    public abstract void load();

    public abstract T get(String path);
}
