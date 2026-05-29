package ua.mazik.javarune.controls;

import java.util.Arrays;
import java.util.List;

import static org.lwjgl.sdl.SDLKeyboard.*;
import static org.lwjgl.sdl.SDLScancode.*;

public enum Key {
    RIGHT(SDL_SCANCODE_RIGHT),
    LEFT(SDL_SCANCODE_LEFT),
    DOWN(SDL_SCANCODE_DOWN),
    UP(SDL_SCANCODE_UP),

    CONFIRM(SDL_SCANCODE_Z),
    CANCEL(SDL_SCANCODE_X),
    MENU(SDL_SCANCODE_C);

    private final List<Integer> glfwKeys;
    private boolean wasPressed;

    Key(int... glfwKeys) {
        this.glfwKeys = Arrays.stream(glfwKeys).boxed().toList();
    }

    public static List<Key> pressed() {
        return Arrays.stream(Key.values()).filter(Key::isPressed).toList();
    }

    public static List<Key> held() {
        return Arrays.stream(Key.values()).filter(Key::isHeld).toList();
    }

    public boolean isPressed() {
        boolean down = false;

        for (int glfwKey : this.glfwKeys) {
            if (isKeyDown(glfwKey)) {
                down = true;
                break;
            }
        }

        boolean justPressed = down && !this.wasPressed;
        this.wasPressed = down;

        return justPressed;
    }

    public boolean isHeld() {
        for (int glfwKey : this.glfwKeys) {
            if (isKeyDown(glfwKey)) {
                return true;
            }
        }
        return false;
    }

    private boolean isKeyDown(int scancode) {
        return SDL_GetKeyboardState().get(scancode) != 0;
    }
}
