package ua.mazik.javarune;

import org.lwjgl.sdl.*;
import ua.mazik.delta.assets.AssetSource;
import ua.mazik.delta.assets.source.ClassAssetSource;
import ua.mazik.delta.fs.DeltaFolder;
import ua.mazik.delta.sdl.audio.SDLAudioDevice;
import ua.mazik.delta.sdl.renderer.SDLPresentationMode;
import ua.mazik.delta.sdl.renderer.SDLRenderer;
import ua.mazik.delta.sdl.util.SDLUtil;
import ua.mazik.delta.sdl.window.SDLWindow;
import ua.mazik.delta.util.Pixel;
import ua.mazik.javarune.assets.AssetHelper;
import ua.mazik.javarune.assets.loader.*;
import ua.mazik.javarune.screen.MainMenuScreen;
import ua.mazik.javarune.screen.Screen;
import ua.mazik.javarune.settings.JavaruneSettings;
import ua.mazik.javarune.util.AtlasManager;
import ua.mazik.javarune.util.LanguageManager;

import static org.lwjgl.sdl.SDLEvents.*;
import static org.lwjgl.sdl.SDLInit.*;
import static org.lwjgl.sdl.SDLKeycode.*;

public final class Javarune {
    public static final DeltaFolder APP_DATA = DeltaFolder.appData("DeltaEngine", "Javarune").orElseThrow();
    public static final JavaruneSettings SETTINGS = new JavaruneSettings(APP_DATA.file("settings.json"));

    public static final int WINDOW_WIDTH = 640;
    public static final int WINDOW_HEIGHT = 480;

    public static final int MIN_WINDOW_WIDTH = 320;
    public static final int MIN_WINDOW_HEIGHT = 240;

    public static final float RENDER_SCALE = 0.5f;

    public static final int LOGICAL_RENDER_WIDTH = 320;
    public static final int LOGICAL_RENDER_HEIGHT = 240;

    private static SDLWindow window;
    private static SDLRenderer renderer;
    private static SDLAudioDevice audioDevice;

    private static AssetSource assetSource;

    private static FontLoader fontLoader;
    private static ImageLoader imageLoader;
    private static JsonLoader jsonLoader;
    private static SoundLoader soundLoader;
    private static TextureLoader textureLoader;

    private static AtlasManager atlasManager;
    private static LanguageManager languageManager;

    private static Screen screen;

    private Javarune() {
    }

    private static void init() {
        SDLUtil.check(SDL_Init(SDL_INIT_VIDEO | SDL_INIT_AUDIO));

        window = SDLWindow.builder()
            .title("Javarune")
            .size(WINDOW_WIDTH, WINDOW_HEIGHT)
            .minSize(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT)
            .centered()
            .hidden()
            .build();

        renderer = new SDLRenderer(window, SDLUtil.getDefaultDriver());

        renderer.useVsync();
        renderer.setRenderScale(RENDER_SCALE, RENDER_SCALE);
        renderer.setRenderLogicalPresentation(LOGICAL_RENDER_WIDTH, LOGICAL_RENDER_HEIGHT, SDLPresentationMode.INTEGER_SCALE);

        audioDevice = new SDLAudioDevice();

        fontLoader = new FontLoader(assetSource);
        imageLoader = new ImageLoader(assetSource);
        jsonLoader = new JsonLoader(assetSource);
        soundLoader = new SoundLoader(assetSource);
        textureLoader = new TextureLoader(assetSource, renderer);

        atlasManager = new AtlasManager(renderer);
        languageManager = new LanguageManager();

        screen = new MainMenuScreen();

        window.show();
    }

    private static boolean update() {
        renderer.setDrawColor(Pixel.BLACK);
        renderer.clear();

        if (screen != null) {
            screen.render();
        }

        renderer.present();

        audioDevice.update();
        AssetHelper.playSound("menumove");

        return true;
    }

    public static SDLRenderer renderer() {
        return renderer;
    }

    public static SDLAudioDevice audioDevice() {
        return audioDevice;
    }

    public static AssetSource assetSource() {
        return assetSource;
    }

    public static FontLoader fontLoader() {
        return fontLoader;
    }

    public static ImageLoader imageLoader() {
        return imageLoader;
    }

    public static JsonLoader jsonLoader() {
        return jsonLoader;
    }

    public static SoundLoader soundLoader() {
        return soundLoader;
    }

    public static TextureLoader textureLoader() {
        return textureLoader;
    }

    public static AtlasManager atlasManager() {
        return atlasManager;
    }

    public static LanguageManager languageManager() {
        return languageManager;
    }

    private static boolean event(SDL_Event event) {
        switch (event.type()) {
            case SDL_EVENT_QUIT -> {
                return false;
            }
            case SDL_EVENT_KEY_DOWN -> {
                if (event.key().key() == SDLK_F4 && !event.key().repeat()) {
                    window.toggleFullscreen();
                }
                if (screen != null) {
                    screen.onKeyDown(event.key().key(), event.key().repeat());
                }
            }
            case SDL_EVENT_KEY_UP -> {
                if (screen != null) {
                    screen.onKeyUp(event.key().key(), event.key().repeat());
                }
            }
        }
        return true;
    }

    private static void quit() {
        SETTINGS.write();

        audioDevice.close();

        window.close();

        atlasManager.close();

        textureLoader.close();
        soundLoader.close();
        imageLoader.close();
        fontLoader.close();

        renderer.close();
    }

    public static void main(String[] args) {
        SETTINGS.read();

        assetSource = new ClassAssetSource(Javarune.class);

        SDLUtil.runWithCallbacks(
            Javarune::init,
            Javarune::update,
            Javarune::event,
            Javarune::quit
        );
    }
}
