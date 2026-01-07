package ua.mazik.javarune.controls;

import org.lwjgl.glfw.GLFW;
import ua.mazik.javarune.Javarune;

import java.util.Arrays;
import java.util.List;

public enum Key {
    RIGHT(GLFW.GLFW_KEY_RIGHT),
    LEFT(GLFW.GLFW_KEY_LEFT),
    DOWN(GLFW.GLFW_KEY_DOWN),
    UP(GLFW.GLFW_KEY_UP),

    CONFIRM(GLFW.GLFW_KEY_Z),
    CANCEL(GLFW.GLFW_KEY_X),
    MENU(GLFW.GLFW_KEY_C);

    private final List<Integer> glfwKeys;
    private boolean wasDown;

    Key(int... glfwKeys) {
        this.glfwKeys = Arrays.stream(glfwKeys).boxed().toList();
    }

    public boolean isPressed() {
        boolean down = false;

        for (int glfwKey : glfwKeys) {
            if (GLFW.glfwGetKey(Javarune.getInstance().window.handle, glfwKey) == GLFW.GLFW_PRESS) {
                down = true;
                break;
            }
        }

        boolean justPressed = down && !this.wasDown;
        this.wasDown = down;

        return justPressed;
    }

    public boolean isHeld() {
        for (int glfwKey : glfwKeys) {
            if (GLFW.glfwGetKey(Javarune.getInstance().window.handle, glfwKey) != GLFW.GLFW_RELEASE) {
                return true;
            }
        }
        return false;
    }
}
