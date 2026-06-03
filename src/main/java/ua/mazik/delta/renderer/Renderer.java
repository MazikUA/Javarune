package ua.mazik.delta.renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;
import ua.mazik.delta.GLFWWindow;
import ua.mazik.delta.renderer.draw.DrawElement;
import ua.mazik.delta.renderer.vertex.VertexFormat;
import ua.mazik.delta.renderer.vertex.VertexType;
import ua.mazik.delta.util.Pixel;

import java.nio.FloatBuffer;
import java.util.List;

public class Renderer {
    private static final FloatBuffer projectionMatrixBuffer = BufferUtils.createFloatBuffer(16);
    private static boolean initialized = false;
    private static int vao;
    private static int vbo;
    private static int ebo;
    private static VertexFormat<?> lastFormat;

    private Renderer() {
    }

    public static void init(GLFWWindow window) {
        if (initialized) return;

        GLFW.glfwMakeContextCurrent(window.handle);
        GL.createCapabilities();

        vao = GL33.glGenVertexArrays();
        vbo = GL33.glGenBuffers();
        ebo = GL33.glGenBuffers();

        GL33.glEnable(GL33.GL_BLEND);
        GL33.glBlendFunc(GL33.GL_SRC_ALPHA, GL33.GL_ONE_MINUS_SRC_ALPHA);

        GL33.glBindVertexArray(vao);

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

    @SuppressWarnings("unchecked")
    public static void drawElements(List<DrawElement<?>> elements) {
        assertIsInitialized();

        DrawElement<?> previous = null;
        VertexBuilder<?> builder = null;

        for (DrawElement<?> element : elements) {
            VertexFormat<?> format = element.format();

            if (builder == null || previous.format() != format) {
                builder = new VertexBuilder<>(format);
            }

            if (previous != null && element.isDirty(previous)) {
                drawElement(previous, builder);
                builder = new VertexBuilder<>(format);
            }

            ((DrawElement<Object>) element).build((VertexBuilder<Object>) builder);

            previous = element;
        }

        if (previous != null) drawElement(previous, builder);
    }

    private static void drawElement(DrawElement<?> element, VertexBuilder<?> builder) {
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vbo);
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, builder.createVertexBuffer(), GL33.GL_STATIC_DRAW);

        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, builder.createIndexBuffer(), GL33.GL_STATIC_DRAW);

        if (lastFormat != element.format()) {
            if (lastFormat == null) {
                for (int i = 0; i < element.format().attributes.size(); i++) {
                    GL33.glEnableVertexAttribArray(i);
                }
            } else {
                if (lastFormat.attributes.size() > element.format().attributes.size()) {
                    int difference = lastFormat.attributes.size() - element.format().attributes.size();

                    for (int i = difference; i < lastFormat.attributes.size(); i++) {
                        GL33.glDisableVertexAttribArray(i);
                    }
                } else if (lastFormat.attributes.size() < element.format().attributes.size()) {
                    int difference = element.format().attributes.size() - lastFormat.attributes.size();

                    for (int i = difference; i < element.format().attributes.size(); i++) {
                        GL33.glEnableVertexAttribArray(i);
                    }
                }
            }
        }

        int index = 0;
        int offset = 0;

        for (VertexFormat.Attribute<?, ?> attribute : element.format().attributes) {
            VertexType<?> type = attribute.type();

            if (type.isInteger()) {
                GL33.glVertexAttribIPointer(index, type.size(), type.glType(), element.format().stride, offset);
            } else {
                GL33.glVertexAttribPointer(index, type.size(), type.glType(), attribute.normalized(), element.format().stride, offset);
            }
            index++;
            offset += type.size() * type.byteSize();
        }

        element.bind();

        GL33.glDrawElements(GL33.GL_TRIANGLES, builder.indexCount(), GL33.GL_UNSIGNED_INT, 0);

        element.unbind();

        lastFormat = element.format();
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
