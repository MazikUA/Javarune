package eu.mazikkk.javarune;

import eu.mazikkk.delta.GLFWWindow;
import eu.mazikkk.delta.Renderer;
import eu.mazikkk.delta.Texture;
import eu.mazikkk.delta.Viewport;
import eu.mazikkk.delta.util.Color;
import eu.mazikkk.javarune.asset.AssetManager;
import eu.mazikkk.javarune.asset.loader.FontLoader;
import eu.mazikkk.javarune.asset.loader.ShaderLoader;
import eu.mazikkk.javarune.asset.loader.TextureLoader;
import eu.mazikkk.javarune.asset.source.JarAssetSource;
import org.jspecify.annotations.NonNull;
import org.lwjgl.glfw.GLFW;

public class Javarune implements AutoCloseable {
    private static Javarune instance;

    public final GLFWWindow window;
    public final Viewport viewport;

    public final AssetManager assetManager;

    public final TextureLoader textureLoader;
    public final ShaderLoader shaderLoader;
    public final FontLoader fontLoader;

    public Javarune(GLFWWindow window) {
        instance = this;

        GLFW.glfwSwapInterval(1);

        this.window = window;
        this.viewport = window.renderer.createViewport(640, 480, Viewport.Scaling.FIT);
        this.viewport.apply();

        window.renderer.setProjectionMatrix(this.viewport.camera.getProjectionMatrix());

        this.assetManager = new AssetManager();
        this.textureLoader = new TextureLoader(assetManager);
        this.assetManager.registerLoader(this.textureLoader);
        this.shaderLoader = new ShaderLoader(assetManager);
        this.assetManager.registerLoader(this.shaderLoader);
        this.fontLoader = new FontLoader(assetManager);
        this.assetManager.registerLoader(this.fontLoader);
        this.assetManager.registerSource(new JarAssetSource(Javarune.class));
        this.assetManager.load();

        window.windowSizeCallback = (handle, windowWidth, windowHeight) -> {
            this.viewport.resize(windowWidth, windowHeight);
            this.viewport.apply();
        };

        window.show();
    }

    public static Javarune getInstance() {
        return instance;
    }

    public static @NonNull Texture texture(String path) {
        return getInstance().textureLoader.get(path);
    }

    public void render(Renderer renderer) {
        renderer.setClearColor(Color.fromRgb(0x000000));
        renderer.clear();

        this.viewport.enableScissors();

        renderer.setClearColor(Color.fromRgb(0x505050));
        renderer.clear();
        renderer.drawTexture(
                texture("misc/logo"),
                shaderLoader.get("texture").orElseThrow(),
                60, 60,
                0, 0,
                219, 24,
                219, 24,
                219, 24
        );

        fontLoader.get("default").draw(renderer, " !\"#$%&'()*+,-./", 50, 120);
        fontLoader.get("default").draw(renderer, "0123456789:;<=>?", 50, 140);
        fontLoader.get("default").draw(renderer, "@ABCDEFGHIJKLMNO", 50, 160);
        fontLoader.get("default").draw(renderer, "PQRSTUVWXYZ[\\]^_", 50, 180);
        fontLoader.get("default").draw(renderer, "`abcdefghijklmno", 50, 200);
        fontLoader.get("default").draw(renderer, "pqrstuvwxyz{|}` ", 50, 220);

        this.viewport.disableScissors();
    }

    @Override
    public void close() {
        this.assetManager.close();
        this.window.close();
    }
}
