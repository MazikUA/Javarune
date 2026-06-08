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
import ua.mazik.javarune.assets.loader.FontLoader;
import ua.mazik.javarune.assets.loader.ImageLoader;
import ua.mazik.javarune.assets.loader.JsonLoader;
import ua.mazik.javarune.assets.loader.TextureLoader;
import ua.mazik.javarune.settings.JavaruneSettings;
import ua.mazik.javarune.text.LiteralText;
import ua.mazik.javarune.text.TextRenderer;
import ua.mazik.javarune.text.font.Font;
import ua.mazik.javarune.util.AtlasManager;
import ua.mazik.javarune.util.LanguageManager;

import java.util.List;
import java.util.Map;

import static org.lwjgl.sdl.SDLEvents.*;
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

    private static AssetSource assetSource;

    private static FontLoader fontLoader;
    private static ImageLoader imageLoader;
    private static JsonLoader jsonLoader;
    private static TextureLoader textureLoader;

    private static AtlasManager atlasManager;
    private static LanguageManager languageManager;

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

        fontLoader = new FontLoader(assetSource);
        imageLoader = new ImageLoader(assetSource);
        jsonLoader = new JsonLoader(assetSource);
        textureLoader = new TextureLoader(assetSource, renderer);

        atlasManager = new AtlasManager(renderer);
        languageManager = new LanguageManager();

        window.show();
    }

    private static boolean update() {
        renderer.setDrawColor(Pixel.BLACK);
        renderer.clear();

        List<String> strings = List.of(
            " !\"#$%&'()*+,-./",
            "0123456789:;<=>?",
            "@ABCDEFGHIJKLMNO",
            "PQRSTUVWXYZ[\\]^_",
            "`abcdefghijklmno",
            "pqrstuvwxyz{|}` ",
            "тест бляха муха її ЇЇ",
            "български език",
            "ДЛФ",
            "вгджзийклптцшщю"
        );

        int textY = 40;

        for (String str : strings) {
            TextRenderer.renderText(new LiteralText(str).color(Pixel.UNNAMED_A), 30, textY);

            textY += 40;
        }

        textY = 40;

        for (Map.Entry<String, String> entry : languageManager.languageNames.entrySet()) {
            TextRenderer.renderText(new LiteralText(entry.getValue()).overrides(Font.Condition.LANGUAGES, List.of(entry.getKey())), 300, textY);

            textY += 40;
        }

        renderer.present();

        return true;
    }

    public static SDLRenderer renderer() {
        return renderer;
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
                switch (event.key().key()) {
                    case SDLK_F4 -> window.toggleFullscreen();
                    case SDLK_E -> {
                        SETTINGS.language.set("bg_bg");
                        languageManager.reloadLanguage();
                    }
                }
            }
        }
        return true;
    }

    private static void quit() {
        SETTINGS.write();

        window.close();

        atlasManager.close();

        fontLoader.close();
        imageLoader.close();
        textureLoader.close();

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
