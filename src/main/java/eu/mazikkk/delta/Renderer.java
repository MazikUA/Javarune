package eu.mazikkk.delta;

import eu.mazikkk.delta.codec.Codec;
import eu.mazikkk.delta.util.Color;
import eu.mazikkk.delta.util.Pixmap;
import eu.mazikkk.delta.util.TextureAtlas;
import org.joml.Matrix4f;
import org.jspecify.annotations.NonNull;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.function.Function;

public abstract class Renderer {
    public final GLFWWindow window;

    public Renderer(GLFWWindow window) {
        this.window = window;
    }

    public abstract void setClearColor(@NonNull Color color);

    public abstract void clear();

    public abstract void setProjectionMatrix(@NonNull Matrix4f projectionMatrix);

    public abstract @NonNull Texture createTexture(int width, int height, @NonNull ByteBuffer buffer);

    public abstract void drawTexture(@NonNull Texture texture, @NonNull ShaderProgram shader, int x, int y, int u, int v, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight);

    public void drawRegion(TextureAtlas.@NonNull Region region, @NonNull ShaderProgram shader, int x, int y, int width, int height) {
        this.drawTexture(region.page().getTexture(), shader, x, y, region.x(), region.y(), width, height, region.width(), region.height(), region.page().width, region.page().height);
    }

    public abstract void subImage(@NonNull Texture texture, @NonNull Pixmap pixmap, int x, int y, int width, int height);

    public abstract @NonNull Viewport createViewport(int width, int height, Viewport.@NonNull Scaling scaling);

    public abstract @NonNull ShaderProgram createShader(@NonNull ShaderDefinition definition, @NonNull Function<String, Optional<InputStream>> reader);

    public abstract @NonNull Codec<? extends ShaderDefinition> getShaderDefinitionCodec();
}
