package eu.mazikkk.javarune.asset.loader;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import eu.mazikkk.javarune.asset.AssetLoader;
import eu.mazikkk.javarune.asset.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TextureLoader extends AssetLoader<Texture> {
    private final Map<String, Texture> textures = new HashMap<>();

    public TextureLoader(AssetManager assetManager) {
        super("textures", assetManager);
    }

    @Override
    public void load() {
        this.dispose();
    }

    @Override
    public Texture get(String path) {
        Texture texture = this.textures.get(path);

        if (texture == null) {
            Optional<InputStream> asset = this.assetManager.findAsset("textures/" + path);

            if (asset.isPresent()) {
                try (InputStream in = asset.get()) {
                    byte[] bytes = in.readAllBytes();
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

    @Override
    public void dispose() {
        this.textures.values().forEach(Texture::dispose);
        this.textures.clear();
    }
}
