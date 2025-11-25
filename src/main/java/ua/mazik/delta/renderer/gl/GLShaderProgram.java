package ua.mazik.delta.renderer.gl;

import org.lwjgl.opengl.GL33;
import ua.mazik.delta.renderer.ShaderProgram;

public class GLShaderProgram implements ShaderProgram {
    public final int program;

    protected GLShaderProgram(int program) {
        this.program = program;
    }

    @Override
    public void apply() {
        GL33.glUseProgram(this.program);
    }

    @Override
    public void close() {
        GL33.glDeleteProgram(this.program);
    }
}
