package ua.mazik.delta;

import org.jspecify.annotations.NonNull;
import org.lwjgl.sdl.*;
import ua.mazik.delta.renderer.Renderer;

import static org.lwjgl.sdl.SDLEvents.*;
import static org.lwjgl.sdl.SDLVideo.*;

// при написанні цієї дурні розробник (поки що) не випав із вікна жодного разу
public class SDLWindow implements AutoCloseable {
    public final long handle;

    public @NonNull ResizeCallback windowSizeCallback = (width, height) -> {
    };

    private int width;
    private int height;

    private boolean running = true;

    public SDLWindow(int width, int height, String title) {
        this.width = width;
        this.height = height;

        this.handle = SDL_CreateWindow("Javarune", width, height, SDL_WINDOW_HIDDEN | SDL_WINDOW_RESIZABLE);
    }

    public void show() {
        SDL_ShowWindow(this.handle);
    }

    public void loop(Runnable runnable) {
        try (SDL_Event event = SDL_Event.malloc()) {
            while (this.running) {
                while (SDL_PollEvent(event)) {
                    switch (event.type()) {
                        case SDL_EVENT_WINDOW_RESIZED -> {
                            this.windowSizeCallback(event.window().data1(), event.window().data2());
                        }
                        case SDL_EVENT_QUIT -> {
                            this.running = false;
                        }
                    }
                }

                runnable.run();
                Renderer.present();
            }
        }
    }

    private void windowSizeCallback(int windowWidth, int windowHeight) {
        this.width = windowWidth;
        this.height = windowHeight;

        windowSizeCallback.onResize(windowWidth, windowHeight);
    }

    @Override
    public void close() {
        SDL_DestroyWindow(this.handle);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    @FunctionalInterface
    public interface ResizeCallback {
        void onResize(int width, int height);
    }
}
