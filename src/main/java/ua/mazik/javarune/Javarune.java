package ua.mazik.javarune;

import org.lwjgl.sdl.*;
import ua.mazik.delta.assets.AssetSource;
import ua.mazik.delta.assets.source.ClassAssetSource;
import ua.mazik.delta.fs.DeltaFolder;
import ua.mazik.delta.sdl.renderer.SDLPresentationMode;
import ua.mazik.delta.sdl.renderer.SDLRenderer;
import ua.mazik.delta.sdl.util.SDLUtil;
import ua.mazik.delta.sdl.window.SDLWindow;
import ua.mazik.delta.util.Pixel;
import ua.mazik.javarune.assets.loader.TextureLoader;
import ua.mazik.javarune.settings.JavaruneSettings;

import static org.lwjgl.sdl.SDLEvents.*;
import static org.lwjgl.sdl.SDLKeycode.*;

public final class Javarune {
    public static final DeltaFolder appData = DeltaFolder.appData("DeltaEngine", "Javarune").orElseThrow();
    public static final JavaruneSettings settings = new JavaruneSettings(appData.file("settings.json"));

    public static final int WINDOW_WIDTH = 640;
    public static final int WINDOW_HEIGHT = 480;

    public static final int MIN_WINDOW_WIDTH = 320;
    public static final int MIN_WINDOW_HEIGHT = 240;

    public static final float RENDER_SCALE = 0.5f;

    public static final int LOGICAL_RENDER_WIDTH = 320;
    public static final int LOGICAL_RENDER_HEIGHT = 240;

    private static SDLWindow window;
    private static SDLRenderer renderer;

    private static AssetSource assetSource;
    private static TextureLoader textureLoader;

    private static int x;
    private static int y;

    private Javarune() {
    }

    private static void init() {
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

        textureLoader = new TextureLoader(assetSource, renderer);

        window.show();
    }

    private static boolean update() {
        renderer.setDrawColor(Pixel.BLACK);
        renderer.clear();

        renderer.setDrawColor(Pixel.RED);
        renderer.drawFillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        textureLoader.get("font/default/ascii").ifPresent(ascii -> {
            ascii.draw(x, y, 144 * 2, 96 * 2);
        });

        renderer.present();

        return true;
    }

    private static boolean event(SDL_Event event) {
        switch (event.type()) {
            case SDL_EVENT_QUIT -> {
                return false;
            }
            case SDL_EVENT_KEY_DOWN -> {
                switch (event.key().key()) {
                    case SDLK_F4 -> window.toggleFullscreen();
                    case SDLK_LEFT -> x--;
                    case SDLK_RIGHT -> x++;
                    case SDLK_UP -> y--;
                    case SDLK_DOWN -> y++;
                }
            }
        }
        return true;
    }

    private static void quit() {
        settings.write();

        window.close();
        textureLoader.close();
        renderer.close();
    }

    public static void main(String[] args) {
        settings.read();

        assetSource = new ClassAssetSource(Javarune.class);

        SDLUtil.runWithCallbacks(
            Javarune::init,
            Javarune::update,
            Javarune::event,
            Javarune::quit
        );
    }
}
