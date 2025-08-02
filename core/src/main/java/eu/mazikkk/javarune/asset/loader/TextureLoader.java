package eu.mazikkk.javarune.asset.loader;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Null;
import eu.mazikkk.javarune.asset.AssetLoader;
import eu.mazikkk.javarune.asset.AssetManager;
import eu.mazikkk.javarune.codec.Codec;
import eu.mazikkk.javarune.codec.Codecs;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TextureLoader extends AssetLoader<Texture> {
    private final Map<String, Texture> textures = new HashMap<>();
    public final Codec<Texture> textureCodec = Codecs.STRING.map(this::get, this::getKey);

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
            Optional<InputStream> asset = this.assetManager.findAsset("textures/" + path + ".png");

            if (asset.isPresent()) {
                try (InputStream in = asset.get()) {
                    byte[] bytes = in.readAllBytes();
                    Pixmap pixmap = new Pixmap(bytes, 0, bytes.length);
                    Texture newTexture = new Texture(pixmap);
                    pixmap.dispose();
                    this.textures.put(path, newTexture);
                    return newTexture;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return this.get("misc/assgore");
        } else {
            return texture;
        }
    }

    @Override
    public void dispose() {
        this.textures.values().forEach(Texture::dispose);
        this.textures.clear();
    }

    @Null
    public String getKey(Texture texture) {
        for (Map.Entry<String, Texture> entry : this.textures.entrySet()) {
            if (entry.getValue() == texture) {
                return entry.getKey();
            }
        }
        return null;
    }
}
