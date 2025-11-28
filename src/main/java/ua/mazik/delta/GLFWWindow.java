package ua.mazik.delta;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.system.MemoryUtil;
import ua.mazik.delta.renderer.Renderer;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

// при написанні цієї дурні розробник (поки що) не випав із вікна жодного разу
public class GLFWWindow implements AutoCloseable {
    public final long handle;

    public GLFWWindowSizeCallbackI windowSizeCallback = (window, windowWidth, windowHeight) -> {
    };
    public GLFWKeyCallbackI keyCallback = (window, key, scancode, action, mods) -> {
    };

    private int width;
    private int height;

    @SuppressWarnings("resource")
    public GLFWWindow(int width, int height, String title) {
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);

        this.width = width;
        this.height = height;

        this.handle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);

        GLFW.glfwSetWindowSizeCallback(this.handle, this::windowSizeCallback);
        GLFW.glfwSetKeyCallback(this.handle, this::keyCallback);

        Renderer.init(this);
    }

    public void show() {
        GLFW.glfwShowWindow(this.handle);
    }

    public void loop(Runnable rendererConsumer) {
        while (!GLFW.glfwWindowShouldClose(this.handle)) {
            GLFW.glfwPollEvents();

            rendererConsumer.run();

            GLFW.glfwSwapBuffers(this.handle);
        }
    }

    private void windowSizeCallback(long window, int windowWidth, int windowHeight) {
        this.width = windowWidth;
        this.height = windowHeight;

        windowSizeCallback.invoke(window, windowWidth, windowHeight);
    }

    private void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
            glfwSetWindowShouldClose(window, true);
        }

        keyCallback.invoke(window, key, scancode, action, mods);
    }

    @Override
    public void close() {
        Callbacks.glfwFreeCallbacks(this.handle);
        GLFW.glfwDestroyWindow(this.handle);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
