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

    public final FontLoader fontLoader;
    public final ShaderLoader shaderLoader;
    public final SoundLoader soundLoader;
    public final TextureLoader textureLoader;

    public Javarune(@NonNull GLFWWindow window) {
        instance = this;

        GLFW.glfwSwapInterval(1);

        this.window = window;
        this.viewport = new Viewport(640, 480, Viewport.Scaling.FIT_PIXEL_PERFECT);
        this.viewport.apply();

        Renderer.setProjectionMatrix(this.viewport.projectionMatrix);

        this.assetManager = new AssetManager();

        this.fontLoader = this.assetManager.registerLoader(FontLoader::new);
        this.shaderLoader = this.assetManager.registerLoader(ShaderLoader::new);
        this.soundLoader = this.assetManager.registerLoader(SoundLoader::new);
        this.textureLoader = this.assetManager.registerLoader(TextureLoader::new);

        this.assetManager.registerSource(new JarAssetSource(Javarune.class));
        this.assetManager.load();

        window.windowSizeCallback = (handle, windowWidth, windowHeight) -> {
            this.viewport.resize(windowWidth, windowHeight);
            this.viewport.apply();
        };

        window.show();
    }

    public static @NonNull Javarune getInstance() {
        return instance;
    }

    public static @NonNull Texture texture(@NonNull String path) {
        return getInstance().textureLoader.get(path);
    }

    public static @NonNull Font font(@NonNull String path) {
        return getInstance().fontLoader.get(path);
    }

    public static void playSound(@NonNull String path) {
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

        ctx.drawText(" !\"#$%&'()*+,-./", 50, 120, Pixel.rgb(0x00AEFF));
        ctx.drawText("0123456789:;<=>?", 50, 140, Pixel.rgb(0x00AEFF));
        ctx.drawText("@ABCDEFGHIJKLMNO", 50, 160, Pixel.rgb(0x00AEFF));
        ctx.drawText("PQRSTUVWXYZ[\\]^_", 50, 180, Pixel.rgb(0x00AEFF));
        ctx.drawText("`abcdefghijklmno", 50, 200, Pixel.rgb(0x00AEFF));
        ctx.drawText("pqrstuvwxyz{|}` ", 50, 220, Pixel.rgb(0x00AEFF));

        Renderer.drawElements(ctx.elements);

        this.viewport.disableScissors();
    }

    @Override
    public void close() {
        this.assetManager.close();
        this.window.close();

        Renderer.shutdown();
    }
}
