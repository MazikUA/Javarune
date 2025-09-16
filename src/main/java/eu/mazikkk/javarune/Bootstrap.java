package eu.mazikkk.javarune;

import eu.mazikkk.delta.GLFWWindow;
import eu.mazikkk.delta.gl.GLRenderer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.MemoryUtil;

public class Bootstrap {
    public static void main(String[] args) {
        //noinspection unused
        try (GLFWErrorCallback callback = GLFWErrorCallback.create(Bootstrap::logError).set()) {
            if (!GLFW.glfwInit()) {
                return;
            }

            GLFWWindow window = new GLFWWindow(640, 480, "Javarune", GLRenderer::new);
            Javarune javarune = new Javarune(window);

            window.loop(javarune::render);
            javarune.close();

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
