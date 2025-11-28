package ua.mazik.delta.renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;
import ua.mazik.delta.GLFWWindow;
import ua.mazik.delta.renderer.draw.DrawElement;
import ua.mazik.delta.util.Pixel;

import java.nio.FloatBuffer;
import java.util.List;

public class Renderer {
    private static final FloatBuffer projectionMatrixBuffer = BufferUtils.createFloatBuffer(16);
    private static boolean initialized = false;
    private static GLFWWindow currentWindow;
    private static int vao;
    private static int vbo;
    private static int ebo;

    private Renderer() {
    }

    public static void init(GLFWWindow window) {
        if (initialized) return;

        currentWindow = window;

        GLFW.glfwMakeContextCurrent(window.handle);
        GL.createCapabilities();

        vao = GL33.glGenVertexArrays();
        vbo = GL33.glGenBuffers();
        ebo = GL33.glGenBuffers();

        GL33.glEnable(GL33.GL_BLEND);
        GL33.glBlendFunc(GL33.GL_SRC_ALPHA, GL33.GL_ONE_MINUS_SRC_ALPHA);

        initialized = true;
    }

    private static void assertIsInitialized() {
        if (!initialized) throw new RuntimeException();
    }

    public static void setClearColor(Pixel color) {
        assertIsInitialized();

        GL33.glClearColor(
                color.red() / 255f,
                color.green() / 255f,
                color.blue() / 255f,
                color.alpha() / 255f
        );
    }

    public static void clear() {
        assertIsInitialized();

        GL33.glClear(GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);
    }

    public static void setProjectionMatrix(Matrix4f projection) {
        assertIsInitialized();

        projection.get(projectionMatrixBuffer);
    }

    public static void drawElement(List<DrawElement> elements) {
        assertIsInitialized();

        GL33.glBindVertexArray(vao);

        DrawElement previous = null;
        VertexBuilder builder = new VertexBuilder();

        for (DrawElement element : elements) {
            if (previous != null && element.isDirty(previous)) {
                drawElement(previous, builder);
            }

            element.build(builder);

            previous = element;
        }

        if (previous != null) drawElement(previous, builder);
    }

    private static void drawElement(DrawElement element, VertexBuilder builder) {
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vbo);
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, builder.getVertexBuffer(), GL33.GL_STATIC_DRAW);

        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, builder.getIndexBuffer(), GL33.GL_STATIC_DRAW);

        builder.enableVertexAttributes();
        element.bind();

        GL33.glDrawElements(GL33.GL_TRIANGLES, builder.indices.size(), GL33.GL_UNSIGNED_INT, 0);

        element.unbind();
        builder.clear();
    }

    public static void applyDefaultUniforms(Shader shader) {
        GL33.glUniformMatrix4fv(GL33.glGetUniformLocation(shader.program, "projection"), false, projectionMatrixBuffer);
    }

    public static void shutdown() {
        assertIsInitialized();

        if (!initialized) return;

        GL33.glDeleteVertexArrays(vao);
        GL33.glDeleteBuffers(vbo);
        GL33.glDeleteBuffers(ebo);
    }
}
