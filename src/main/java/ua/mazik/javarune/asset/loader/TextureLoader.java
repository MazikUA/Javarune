package ua.mazik.javarune.asset.loader;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import ua.mazik.delta.renderer.Texture;
import ua.mazik.delta.util.Pixmap;
import ua.mazik.javarune.asset.AssetLoader;
import ua.mazik.javarune.asset.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TextureLoader extends AssetLoader<Texture> {
    private final Map<String, Texture> textures = new HashMap<>();
    // public final Codec<Texture> textureCodec = Codecs.STRING.map(this::get, this::getKey);

    public TextureLoader(@NonNull AssetManager assetManager) {
        super("textures", assetManager);
    }

    @Override
    public void load() {
        this.close();
    }

    @Override
    public @NonNull Texture get(@NonNull String path) {
        Texture texture = this.textures.get(path);

        if (texture == null) {
            Optional<InputStream> asset = this.assetManager.findAsset("textures/" + path + ".png");

            if (asset.isPresent()) {
                try (InputStream in = asset.get()) {
                    byte[] bytes = in.readAllBytes();

                    Pixmap pixmap = Pixmap.fromImageBytes(bytes);
                    Texture newTexture = pixmap.toTexture();

                    pixmap.close();

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
    public void close() {
        this.textures.values().forEach(Texture::close);
        this.textures.clear();
    }

    @Nullable
    public String getKey(Texture texture) {
        for (Map.Entry<String, Texture> entry : this.textures.entrySet()) {
            if (entry.getValue() == texture) {
                return entry.getKey();
            }
        }
        return null;
    }
}
