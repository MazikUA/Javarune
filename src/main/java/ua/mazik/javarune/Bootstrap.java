package ua.mazik.javarune;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.MemoryUtil;
import ua.mazik.delta.GLFWWindow;
import ua.mazik.delta.audio.Audio;

public class Bootstrap {
    public static void main(String[] args) {
        //noinspection unused
        try (GLFWErrorCallback callback = GLFWErrorCallback.create(Bootstrap::logError).set()) {
            if (!GLFW.glfwInit()) {
                return;
            }

            GLFWWindow window = new GLFWWindow(640, 480, "Javarune");
            Audio.init();

            Javarune javarune = new Javarune(window);

            window.loop(javarune::update);
            javarune.close();

            Audio.shutdown();

            //noinspection resource
            GLFW.glfwSetErrorCallback(null);
            GLFW.glfwTerminate();
        }
    }

    private static void logError(int error, long description) {
        System.err.println(error);
        System.err.println(MemoryUtil.memUTF8(description));
    }
}
