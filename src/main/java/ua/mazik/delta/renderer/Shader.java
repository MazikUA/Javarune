package ua.mazik.delta.renderer;

import org.lwjgl.opengl.GL33;
import ua.mazik.delta.renderer.vertex.VertexFormat;
import ua.mazik.delta.util.Bindable;

public class Shader implements Bindable, AutoCloseable {
    public final int program;

    public Shader(String vertex, String fragment) {
        int vertexShader = GL33.glCreateShader(GL33.GL_VERTEX_SHADER);
        int fragmentShader = GL33.glCreateShader(GL33.GL_FRAGMENT_SHADER);

        GL33.glShaderSource(vertexShader, vertex);
        GL33.glShaderSource(fragmentShader, fragment);

        GL33.glCompileShader(vertexShader);
        GL33.glCompileShader(fragmentShader);

        this.program = GL33.glCreateProgram();

        GL33.glAttachShader(this.program, vertexShader);
        GL33.glAttachShader(this.program, fragmentShader);

        GL33.glLinkProgram(this.program);

        GL33.glDeleteShader(vertexShader);
        GL33.glDeleteShader(fragmentShader);
    }

    @Override
    public void bind() {
        GL33.glUseProgram(this.program);
    }

    @Override
    public void unbind() {
        GL33.glUseProgram(0);
    }

    public <T> void verify(VertexFormat<T> format) {

    }

    @Override
    public void close() {
        GL33.glDeleteProgram(this.program);
    }
}
