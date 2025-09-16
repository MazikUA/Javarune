package eu.mazikkk.javarune.asset.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import eu.mazikkk.delta.ShaderDefinition;
import eu.mazikkk.delta.ShaderProgram;
import eu.mazikkk.javarune.Javarune;
import eu.mazikkk.javarune.asset.AssetLoader;
import eu.mazikkk.javarune.asset.AssetManager;
import org.jspecify.annotations.NonNull;

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
                    ShaderDefinition definition = Javarune.getInstance().window.renderer.getShaderDefinitionCodec().decode(json);

                    return Optional.of(Javarune.getInstance().window.renderer.createShader(definition, (readerPath) -> this.assetManager.findAsset("shaders/opengl/" + readerPath)));
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
