package ua.mazik.javarune;

import org.jspecify.annotations.NonNull;
import org.lwjgl.glfw.GLFW;
import ua.mazik.delta.GLFWWindow;
import ua.mazik.delta.audio.Sound;
import ua.mazik.delta.renderer.Renderer;
import ua.mazik.delta.renderer.Texture;
import ua.mazik.delta.renderer.Viewport;
import ua.mazik.delta.util.Pixel;
import ua.mazik.javarune.asset.AssetManager;
import ua.mazik.javarune.asset.loader.FontLoader;
import ua.mazik.javarune.asset.loader.ShaderLoader;
import ua.mazik.javarune.asset.loader.SoundLoader;
import ua.mazik.javarune.asset.loader.TextureLoader;
import ua.mazik.javarune.asset.source.JarAssetSource;
import ua.mazik.javarune.font.Font;
import ua.mazik.javarune.render.RenderContext;
import ua.mazik.javarune.render.Shaders;

public class Javarune implements AutoCloseable {
    private static Javarune instance;

    public final GLFWWindow window;
    public final Viewport viewport;

    public final AssetManager assetManager;

    public final TextureLoader textureLoader;
    public final ShaderLoader shaderLoader;
    public final FontLoader fontLoader;
    public final SoundLoader soundLoader;

    public Javarune(GLFWWindow window) {
        instance = this;

        GLFW.glfwSwapInterval(1);

        this.window = window;
        this.viewport = new Viewport(640, 480, Viewport.Scaling.FIT);
        this.viewport.apply();

        Renderer.setProjectionMatrix(this.viewport.projectionMatrix);

        this.assetManager = new AssetManager();
        this.textureLoader = new TextureLoader(assetManager);
        this.assetManager.registerLoader(this.textureLoader);
        this.shaderLoader = new ShaderLoader(assetManager);
        this.assetManager.registerLoader(this.shaderLoader);
        this.fontLoader = new FontLoader(assetManager);
        this.assetManager.registerLoader(this.fontLoader);
        this.soundLoader = new SoundLoader(assetManager);
        this.assetManager.registerLoader(this.soundLoader);
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

    public static @NonNull Font font(String path) {
        return getInstance().fontLoader.get(path);
    }

    public static void playSound(String path) {
        getInstance().soundLoader.get(path).ifPresent(Sound::play);
    }

    public void render() {
        Renderer.setClearColor(Pixel.rgb(0x000000));
        Renderer.clear();

        this.viewport.enableScissors();

        Renderer.setClearColor(Pixel.rgb(0x505050));
        Renderer.clear();

        RenderContext ctx = new RenderContext();

        ctx.drawTexture(
                "misc/logo",
                Shaders.TEXTURE,
                60, 60,
                0, 0,
                219, 24,
                219, 24
        );

        ctx.drawText(" !\"#$%&'()*+,-./", 50, 120);
        ctx.drawText("0123456789:;<=>?", 50, 140);
        ctx.drawText("@ABCDEFGHIJKLMNO", 50, 160);
        ctx.drawText("PQRSTUVWXYZ[\\]^_", 50, 180);
        ctx.drawText("`abcdefghijklmno", 50, 200);
        ctx.drawText("pqrstuvwxyz{|}` ", 50, 220);

        this.viewport.disableScissors();

        Renderer.drawElement(ctx.elements);
    }

    @Override
    public void close() {
        this.assetManager.close();
        this.window.close();

        Renderer.shutdown();
    }
}
