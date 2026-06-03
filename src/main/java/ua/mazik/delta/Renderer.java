package ua.mazik.delta;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import ua.mazik.delta.renderer.Shader;
import ua.mazik.delta.renderer.VertexBuilder;
import ua.mazik.delta.renderer.draw.DrawElement;
import ua.mazik.delta.renderer.vertex.VertexFormat;
import ua.mazik.delta.util.Pixel;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.sdl.SDLRender.*;

public class Renderer {
    private static final FloatBuffer projectionMatrixBuffer = BufferUtils.createFloatBuffer(16);
    public static long address;
    private static boolean initialized = false;
    private static int vao;
    private static int vbo;
    private static int ebo;
    private static VertexFormat<?> lastFormat;

    private Renderer() {
    }

    public static void init(SDLWindow window) {
        if (initialized) return;

        address = SDL_CreateRenderer(window.handle, "opengl");

        if (address == 0) {
            throw new RuntimeException();
        }

        initialized = true;
    }

    private static void assertIsInitialized() {
        if (!initialized) throw new RuntimeException();
    }

    public static void setClearColor(Pixel color) {
        assertIsInitialized();

        SDL_SetRenderDrawColor(
                address,
                (byte) color.red(),
                (byte) color.green(),
                (byte) color.blue(),
                (byte) color.alpha()
        );
    }

    public static void clear() {
        assertIsInitialized();

        SDL_RenderClear(address);
    }

    public static void present() {
        assertIsInitialized();

        SDL_RenderPresent(address);
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
        /* GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vbo);
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

        lastFormat = element.format();*/
    }

    public static void applyDefaultUniforms(Shader shader) {
        /*GL33.glUniformMatrix4fv(GL33.glGetUniformLocation(shader.program, "projection"), false, projectionMatrixBuffer);*/
    }

    public static void shutdown() {
        assertIsInitialized();

        if (!initialized) {
        }
    }
}
