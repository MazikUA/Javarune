package ua.mazik.javarune.asset.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.jspecify.annotations.NonNull;
import ua.mazik.delta.renderer.Renderer;
import ua.mazik.delta.renderer.ShaderDefinition;
import ua.mazik.delta.renderer.ShaderProgram;
import ua.mazik.javarune.asset.AssetLoader;
import ua.mazik.javarune.asset.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ShaderLoader extends AssetLoader<Optional<ShaderProgram>> {
    private final Map<String, ShaderProgram> shaders = new HashMap<>();

    public ShaderLoader(@NonNull AssetManager assetManager) {
        super("shaders", assetManager);
    }

    @Override
    public void load() {
        this.close();
    }

    @Override
    public @NonNull Optional<ShaderProgram> get(@NonNull String path) {
        ShaderProgram texture = this.shaders.get(path);

        if (texture == null) {
            Optional<InputStream> asset = this.assetManager.findAsset("shaders/opengl/" + path + ".json");

            if (asset.isPresent()) {
                try (InputStream in = asset.get()) {
                    byte[] bytes = in.readAllBytes();

                    JsonElement json = JsonParser.parseString(new String(bytes));
                    ShaderDefinition definition = Renderer.getInstance().getShaderDefinitionCodec().decode(json);

                    return Optional.of(Renderer.getInstance().createShader(definition, (readerPath) -> this.assetManager.findAsset("shaders/opengl/" + readerPath)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void close() {
        this.shaders.values().forEach(ShaderProgram::close);
        this.shaders.clear();
    }
}
