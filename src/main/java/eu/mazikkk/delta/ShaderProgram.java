package eu.mazikkk.delta;

public interface ShaderProgram extends AutoCloseable {
    void apply();

    @Override
    void close();
}
