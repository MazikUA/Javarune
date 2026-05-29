package ua.mazik.javarune;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import ua.mazik.delta.SDLWindow;
import ua.mazik.delta.audio.Sound;
import ua.mazik.delta.renderer.Renderer;
import ua.mazik.delta.renderer.Shader;
import ua.mazik.delta.renderer.Texture;
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

import static org.lwjgl.sdl.SDLRender.*;

public class Javarune implements AutoCloseable {
    private static Javarune instance;

    public final SDLWindow window;

    public final AssetManager assetManager;

    public final FontLoader fontLoader;
    public final ShaderLoader shaderLoader;
    public final SoundLoader soundLoader;
    public final TextureLoader textureLoader;

    private Screen screen;

    public Javarune(@NonNull SDLWindow window) {
        instance = this;

        this.window = window;

        SDL_SetRenderLogicalPresentation(Renderer.address, 320, 240, SDL_LOGICAL_PRESENTATION_INTEGER_SCALE);
        SDL_SetRenderScale(Renderer.address, 0.5f, 0.5f);

        this.assetManager = new AssetManager();

        this.fontLoader = this.assetManager.registerLoader(FontLoader::new);
        this.shaderLoader = this.assetManager.registerLoader(ShaderLoader::new);
        this.soundLoader = this.assetManager.registerLoader(SoundLoader::new);
        this.textureLoader = this.assetManager.registerLoader(TextureLoader::new);

        this.assetManager.registerSource(new JarAssetSource(Javarune.class));
        this.assetManager.load();

        this.setScreen(new MainMenuScreen());
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
        Renderer.setDrawColor(Pixel.rgb(0x000000));
        Renderer.clear();

        if (this.screen != null) {
            this.screen.update();
            this.screen.render();
        }
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
