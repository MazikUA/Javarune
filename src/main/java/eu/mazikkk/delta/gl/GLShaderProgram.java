package eu.mazikkk.delta.gl;

import eu.mazikkk.delta.ShaderProgram;
import org.lwjgl.opengl.GL33;

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
