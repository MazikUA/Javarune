package ua.mazik.delta.renderer;

public interface ShaderProgram extends AutoCloseable {
    void apply();

    @Override
    void close();
}
