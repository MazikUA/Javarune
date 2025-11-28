package ua.mazik.javarune.asset.loader;

import org.jspecify.annotations.NonNull;
import ua.mazik.delta.renderer.Shader;
import ua.mazik.javarune.asset.AssetLoader;
import ua.mazik.javarune.asset.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ShaderLoader extends AssetLoader<Optional<Shader>> {
    private final Map<String, Shader> shaders = new HashMap<>();

    public ShaderLoader(@NonNull AssetManager assetManager) {
        super("shaders", assetManager);
    }

    @Override
    public void load() {
        this.close();
    }

    @Override
    public @NonNull Optional<Shader> get(@NonNull String path) {
        Shader texture = this.shaders.get(path);

        if (texture == null) {
            Optional<InputStream> vertex = this.assetManager.findAsset("shaders/vertex/" + path + ".vsh");
            Optional<InputStream> fragment = this.assetManager.findAsset("shaders/fragment/" + path + ".fsh");

            if (fragment.isPresent() && vertex.isPresent()) {
                String vertexString;
                String fragmentString;

                try (InputStream in = vertex.get()) {
                    vertexString = new String(in.readAllBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                    return Optional.empty();
                }

                try (InputStream in = fragment.get()) {
                    fragmentString = new String(in.readAllBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                    return Optional.empty();
                }

                Shader shader = new Shader(vertexString, fragmentString);

                this.shaders.put(path, shader);

                return Optional.of(shader);
            }

            return Optional.empty();
        } else {
            return Optional.of(texture);
        }
    }

    @Override
    public void close() {
        this.shaders.values().forEach(Shader::close);
        this.shaders.clear();
    }
}
