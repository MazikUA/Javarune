package ua.mazik.delta.sdl.util;

import org.lwjgl.sdl.*;
import ua.mazik.delta.sdl.renderer.SDLDriver;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.lwjgl.sdl.SDLError.*;
import static org.lwjgl.sdl.SDLInit.*;
import static org.lwjgl.sdl.SDLMain.*;
import static org.lwjgl.sdl.SDLPlatform.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memPointerBuffer;

/**
 * Utility class for simplified usage of different SDL functions.
 */
public final class SDLUtil {
    private SDLUtil() {
    }

    /**
     * Runs SDL application using new SDL3 callbacks.
     *
     * @param appInit    Function, which will be called on init.
     * @param appIterate Function, which will be called each frame.
     * @param appEvent   Function, which will be called each event.
     * @param appQuit    Function, which will be called on quit.
     */
    public static void runWithCallbacks(Runnable appInit, Supplier<Boolean> appIterate, Function<SDL_Event, Boolean> appEvent, Runnable appQuit) {
        try (SDL_main_func mainFunc = SDL_main_func.create(createMainFuncInstance(appInit, appIterate, appEvent, appQuit))) {
            int result = SDL_RunApp(null, mainFunc, NULL);

            if (result != 0) {
                System.err.println("main() finished with error code " + result);
                System.exit(result);
            }
        }
    }

    /**
     * Throws {@link SDLException} with {@link SDLError#SDL_GetError()} message.
     */
    public static void throwException() {
        throw new SDLException(SDL_GetError());
    }

    /**
     * Checks, is {@code success} true. If not, runs {@link SDLUtil#throwException()}.
     * Should be used with SDL functions, which returns {@code boolean}.
     *
     * @param success Param to check.
     */
    public static void check(boolean success) {
        if (!success) {
            throwException();
        }
    }

    /**
     * Checks, is {@code pointer} not {@code NULL}. If {@code NULL}, runs {@link SDLUtil#throwException()}.
     * Should be used with SDL functions, which returns {@code long}.
     *
     * @param pointer Param to check.
     */
    public static long check(long pointer) {
        if (pointer == NULL) {
            throwException();
        }
        return pointer;
    }

    /**
     * @return {@link SDLDriver} preferred on current platform.
     */
    public static SDLDriver getDefaultDriver() {
        String platform = SDL_GetPlatform();

        if (platform == null) {
            return SDLDriver.OPENGL;
        }

        return switch (platform) {
            case "macOS" -> SDLDriver.METAL;
            case "Linux" -> SDLDriver.VULKAN;
            default -> SDLDriver.OPENGL;
        };
    }

    private static SDL_main_funcI createMainFuncInstance(Runnable appInit, Supplier<Boolean> appIterate, Function<SDL_Event, Boolean> appEvent, Runnable appQuit) {
        return (argc, argv) -> {
            try (
                SDL_AppInit_func appInitFunc = SDL_AppInit_func.create((appState, _argc, _argv) -> {
                    try {
                        appInit.run();
                        return SDL_APP_CONTINUE;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return SDL_APP_FAILURE;
                    }
                });

                SDL_AppIterate_func appIterateFunc = SDL_AppIterate_func.create((appState) -> {
                    try {
                        return appIterate.get() ? SDL_APP_CONTINUE : SDL_APP_SUCCESS;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return SDL_APP_FAILURE;
                    }
                });

                SDL_AppEvent_func appEventFunc = SDL_AppEvent_func.create((appState, event) -> {
                    try {
                        return appEvent.apply(SDL_Event.create(event)) ? SDL_APP_CONTINUE : SDL_APP_SUCCESS;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return SDL_APP_FAILURE;
                    }
                });

                SDL_AppQuit_func appQuitFunc = SDL_AppQuit_func.create((appState, result) -> {
                    try {
                        appQuit.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
            ) {
                return SDL_EnterAppMainCallbacks(
                    memPointerBuffer(argv, argc),
                    appInitFunc,
                    appIterateFunc,
                    appEventFunc,
                    appQuitFunc
                );
            }
        };
    }
}
