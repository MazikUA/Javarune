package ua.mazik.delta.sdl.util;

import java.nio.ByteBuffer;

import static org.lwjgl.sdl.SDLKeyboard.*;

public final class KeyboardState {
    private static final ByteBuffer STATE = SDL_GetKeyboardState();

    private KeyboardState() {
    }

    public static boolean pressed(int scancode) {
        if (STATE == null) return false;

        return STATE.get(scancode) == 1;
    }
}
