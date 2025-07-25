package eu.mazikkk.javarune.asset.loader;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import eu.mazikkk.javarune.asset.AssetLoader;
import eu.mazikkk.javarune.asset.AssetManager;
import eu.mazikkk.javarune.asset.AssetPath;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TextureLoader extends AssetLoader<Texture> {
    private final Map<AssetPath, Texture> textures = new HashMap<>();

    public TextureLoader(AssetManager assetManager) {
        super("textures", assetManager);
    }

    @Override
    public void load() {
        this.dispose();
    }

    @Override
    public Texture get(AssetPath path) {
        Texture texture = this.textures.get(path);

        if (texture == null) {
            Optional<InputStream> asset = this.assetManager.findAsset(path);

            if (asset.isPresent()) {
                try {
                    byte[] bytes = asset.get().readAllBytes();
                    Texture newTexture = new Texture(new Pixmap(bytes, 0, bytes.length));
                    this.textures.put(path, newTexture);
                    return newTexture;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        } else {
            return texture;
        }
    }

    public void disposeTexture(AssetPath path, Texture texture) {
        texture.dispose();
    }

    @Override
    public void dispose() {
        this.textures.forEach(this::disposeTexture);
        this.textures.clear();
    }
}
