package ua.mazik.javarune.asset.loader;

import org.jspecify.annotations.NonNull;
import ua.mazik.delta.renderer.Shader;
import ua.mazik.javarune.asset.AssetLoader;
import ua.mazik.javarune.asset.AssetManager;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class ShaderLoader extends AssetLoader<Shader> {
    private final Map<String, Shader> loadedShaders = new HashMap<>();

    public ShaderLoader(@NonNull AssetManager assetManager) {
        super("shaders", assetManager);
    }

    @Override
    public void load() {
        this.close();

        Map<String, String> vertexShaders = this.getShaders(".vsh");
        Map<String, String> fragmentShaders = this.getShaders(".fsh");

        Set<String> pairs = new HashSet<>(vertexShaders.keySet());
        pairs.retainAll(fragmentShaders.keySet());

        for (String shader : pairs) {
            this.loadedShaders.put(shader, new Shader(vertexShaders.get(shader), fragmentShaders.get(shader)));
        }

        Set<String> missingFrag = new HashSet<>(fragmentShaders.keySet());
        missingFrag.removeAll(vertexShaders.keySet());

        for (String frag : missingFrag) {
            System.out.println("[WARN] Can't find pair for \"" + frag + ".fsh\" shader (\"" + frag + ".vsh\" missing).");
        }

        Set<String> missingVert = new HashSet<>(vertexShaders.keySet());
        missingVert.removeAll(fragmentShaders.keySet());

        for (String vert : missingVert) {
            System.out.println("[WARN] Can't find pair for \"" + vert + ".vsh\" shader (\"" + vert + ".fsh\" missing).");
        }
    }

    private Map<String, String> getShaders(String suffix) {
        return this.assetManager.listAssets("shaders", suffix).stream()
                .map(path -> {
                    Optional<InputStream> is = this.assetManager.findAsset("shaders/" + path);

                    if (is.isEmpty()) return null;

                    try (InputStream stream = is.get()) {
                        String content = new String(stream.readAllBytes());
                        return Map.entry(path.replace(suffix, ""), content);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public @NonNull Shader get(@NonNull String path) {
        return this.loadedShaders.get(path);
    }

    @Override
    public void close() {
        this.loadedShaders.values().forEach(Shader::close);
        this.loadedShaders.clear();
    }
}
