package eu.mazikkk.delta.gl;

import eu.mazikkk.delta.*;
import eu.mazikkk.delta.codec.Codec;
import eu.mazikkk.delta.codec.Codecs;
import eu.mazikkk.delta.codec.RecordCodec;
import eu.mazikkk.delta.util.Color;
import eu.mazikkk.delta.util.Pixmap;
import org.joml.Matrix4f;
import org.jspecify.annotations.NonNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLCapabilities;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Optional;
import java.util.function.Function;

public class GLRenderer extends Renderer {
    public final GLCapabilities capabilities;
    public final Codec<GLShaderDefinition> shaderDefinitionCodec = RecordCodec.create(
            Codecs.STRING.propertyOf("vertex").getter(GLShaderDefinition::vertex),
            Codecs.STRING.propertyOf("fragment").getter(GLShaderDefinition::fragment),
            GLShaderDefinition::new
    ).toCodec();

    public FloatBuffer projectionMatrix = BufferUtils.createFloatBuffer(16);

    public GLRenderer(GLFWWindow window) {
        super(window);

        GLFW.glfwMakeContextCurrent(window.handle);

        this.capabilities = GL.createCapabilities();

        GL33.glMatrixMode(GL33.GL_PROJECTION);
        GL33.glLoadIdentity();
        GL33.glOrtho(0, 640, 480, 0, -1, 1);
        GL33.glMatrixMode(GL33.GL_MODELVIEW);
        GL33.glLoadIdentity();

        GL33.glEnable(GL33.GL_BLEND);
        GL33.glBlendFunc(GL33.GL_SRC_ALPHA, GL33.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void setClearColor(@NonNull Color color) {
        GL33.glClearColor(
                color.red / 255f,
                color.green / 255f,
                color.blue / 255f,
                color.alpha / 255f
        );
    }

    @Override
    public void clear() {
        GL33.glClear(GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void setProjectionMatrix(@NonNull Matrix4f projectionMatrix) {
        projectionMatrix.get(this.projectionMatrix);
    }

    @Override
    public @NonNull Texture createTexture(int width, int height, @NonNull ByteBuffer buffer) {
        int id = GL33.glGenTextures();

        GL33.glBindTexture(GL33.GL_TEXTURE_2D, id);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_NEAREST);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_NEAREST);
        GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGBA8, width, height, 0, GL33.GL_RGBA, GL33.GL_UNSIGNED_BYTE, buffer);

        GL33.glBindTexture(GL33.GL_TEXTURE_2D, 0);

        return new GLTexture(id);
    }

    @Override
    public void drawTexture(@NonNull Texture texture, @NonNull ShaderProgram shader, int x, int y, int u, int v, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        if (!(texture instanceof GLTexture glTexture) || !(shader instanceof GLShaderProgram glShader)) {
            return;
        }

        float x1 = x;
        float y1 = y;
        float x2 = x + width;
        float y2 = y + height;

        float u0 = (float) u / textureWidth;
        float v0 = (float) v / textureHeight;
        float u1 = (float) (u + regionWidth) / textureWidth;
        float v1 = (float) (v + regionHeight) / textureHeight;

        float[] vertices = {
                x1, y1, u0, v0,
                x2, y1, u1, v0,
                x2, y2, u1, v1,
                x1, y2, u0, v1
        };

        int[] indices = {
                0, 1, 3,
                1, 2, 3
        };

        int vao = GL33.glGenVertexArrays();
        int vbo = GL33.glGenBuffers();
        int ebo = GL33.glGenBuffers();

        GL33.glBindVertexArray(vao);

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vbo);
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, vertices, GL33.GL_STATIC_DRAW);

        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, indices, GL33.GL_STATIC_DRAW);

        GL33.glVertexAttribPointer(0, 2, GL33.GL_FLOAT, false, 4 * Float.BYTES, 0);
        GL33.glEnableVertexAttribArray(0);

        GL33.glVertexAttribPointer(1, 2, GL33.GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        GL33.glEnableVertexAttribArray(1);

        GL33.glBindTexture(GL33.GL_TEXTURE_2D, glTexture.id);

        glShader.apply();

        GL33.glUniformMatrix4fv(GL33.glGetUniformLocation(glShader.program, "projection"), false, this.projectionMatrix);

        GL33.glBindVertexArray(vao);
        GL33.glDrawElements(GL33.GL_TRIANGLES, indices.length, GL33.GL_UNSIGNED_INT, 0);

        GL33.glUseProgram(0);
        GL33.glBindVertexArray(0);

        GL33.glDeleteVertexArrays(vao);
        GL33.glDeleteBuffers(vbo);
        GL33.glDeleteBuffers(ebo);
    }

    @Override
    public void subImage(@NonNull Texture texture, @NonNull Pixmap pixmap, int x, int y, int width, int height) {
        if (!(texture instanceof GLTexture glTexture)) {
            return;
        }

        GL33.glBindTexture(GL33.GL_TEXTURE_2D, glTexture.id);
        GL33.glTexSubImage2D(GL33.GL_TEXTURE_2D, 0, x, y, width, height, GL33.GL_RGBA, GL33.GL_UNSIGNED_BYTE, pixmap.getBuffer());
    }

    @Override
    public @NonNull Viewport createViewport(int width, int height, Viewport.@NonNull Scaling scaling) {
        return new GLViewport(width, height, scaling);
    }

    @Override
    public @NonNull ShaderProgram createShader(@NonNull ShaderDefinition definition, @NonNull Function<String, Optional<InputStream>> reader) {
        if (!(definition instanceof GLShaderDefinition glDefinition)) {
            throw new RuntimeException();
        }

        try {
            InputStream vertexStream = reader.apply(glDefinition.vertex() + ".vsh").orElseThrow();
            InputStream fragmentStream = reader.apply(glDefinition.fragment() + ".fsh").orElseThrow();

            int vertexShader = createShader(vertexStream.readAllBytes(), GL33.GL_VERTEX_SHADER);
            int fragmentShader = createShader(fragmentStream.readAllBytes(), GL33.GL_FRAGMENT_SHADER);

            vertexStream.close();
            fragmentStream.close();

            int program = GL33.glCreateProgram();

            GL33.glAttachShader(program, vertexShader);
            GL33.glAttachShader(program, fragmentShader);

            GL33.glLinkProgram(program);

            if (GL33.glGetProgrami(program, GL33.GL_LINK_STATUS) == GL33.GL_FALSE) {
                throw new RuntimeException("Program linking error: " + GL33.glGetProgramInfoLog(program));
            }

            GL33.glDeleteShader(vertexShader);
            GL33.glDeleteShader(fragmentShader);

            return new GLShaderProgram(program);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int createShader(byte[] sourceBytes, int type) {
        String source = new String(sourceBytes);

        int shader = GL33.glCreateShader(type);

        GL33.glShaderSource(shader, source);
        GL33.glCompileShader(shader);

        if (GL33.glGetShaderi(shader, GL33.GL_COMPILE_STATUS) == GL33.GL_FALSE) {
            throw new RuntimeException("Shader compilation failed: " + GL33.glGetShaderInfoLog(shader));
        }

        return shader;
    }

    @Override
    public @NonNull Codec<GLShaderDefinition> getShaderDefinitionCodec() {
        return this.shaderDefinitionCodec;
    }
}
