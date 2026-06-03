package ua.mazik.javarune;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import ua.mazik.delta.Renderer;
import ua.mazik.delta.SDLWindow;
import ua.mazik.delta.audio.Sound;
import ua.mazik.delta.renderer.Shader;
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
import ua.mazik.javarune.screen.MainMenuScreen;
import ua.mazik.javarune.screen.Screen;

public class Javarune implements AutoCloseable {
    private static Javarune instance;

    public final SDLWindow window;
    public final Viewport viewport;

    public final AssetManager assetManager;

    public final FontLoader fontLoader;
    public final ShaderLoader shaderLoader;
    public final SoundLoader soundLoader;
    public final TextureLoader textureLoader;

    private Screen screen;

    public Javarune(@NonNull SDLWindow window) {
        instance = this;

        this.window = window;
        this.viewport = new Viewport(640, 480, Viewport.Scaling.FIT_PIXEL_PERFECT);
        this.viewport.resize(window.getWidth(), window.getHeight());
        this.viewport.apply();

        Renderer.setProjectionMatrix(this.viewport.projectionMatrix);

        this.assetManager = new AssetManager();

        this.fontLoader = this.assetManager.registerLoader(FontLoader::new);
        this.shaderLoader = this.assetManager.registerLoader(ShaderLoader::new);
        this.soundLoader = this.assetManager.registerLoader(SoundLoader::new);
        this.textureLoader = this.assetManager.registerLoader(TextureLoader::new);

        this.assetManager.registerSource(new JarAssetSource(Javarune.class));
        this.assetManager.load();

        this.setScreen(new MainMenuScreen());

        window.windowSizeCallback = (width, height) -> {
            this.viewport.resize(width, height);
            this.viewport.apply();
        };
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

    public static @NonNull Shader shader(@NonNull String path) {
        return getInstance().shaderLoader.get(path);
    }

    public static void playSound(@NonNull String path) {
        getInstance().soundLoader.get(path).ifPresent(Sound::play);
    }

    public void update() {
        Renderer.setClearColor(Pixel.rgb(0x000000));
        Renderer.clear();

        Renderer.setClearColor(Pixel.rgb(0xFF0000));
        this.viewport.drawFillRect();
    }

    @Override
    public void close() {
        this.assetManager.close();

        Renderer.shutdown();
    }

    public @Nullable Screen getScreen() {
        return this.screen;
    }

    public void setScreen(@Nullable Screen screen) {
        this.screen = screen;
    }
}
