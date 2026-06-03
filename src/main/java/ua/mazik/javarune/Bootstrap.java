package ua.mazik.javarune;

import org.lwjgl.system.MemoryUtil;
import ua.mazik.delta.SDLWindow;
import ua.mazik.delta.renderer.Renderer;

import static org.lwjgl.sdl.SDLInit.*;

public class Bootstrap {
    public static void main(String[] args) {
        if (!SDL_Init(SDL_INIT_VIDEO | SDL_INIT_AUDIO)) {
            return;
        }

        try (SDLWindow window = new SDLWindow(640, 480, "Javarune")) {
            Renderer.init(window);

            Javarune javarune = new Javarune(window);

            window.show();
            window.loop(javarune::update);

            javarune.close();
        }

        SDL_Quit();
    }

    private static void logError(int error, long description) {
        System.err.println(error);
        System.err.println(MemoryUtil.memUTF8(description));
    }
}
