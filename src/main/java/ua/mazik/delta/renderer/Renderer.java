package ua.mazik.delta.renderer;

import org.joml.Matrix4f;
import org.jspecify.annotations.NonNull;
import ua.mazik.delta.GLFWWindow;
import ua.mazik.delta.codec.Codec;
import ua.mazik.delta.renderer.gl.GLRenderer;
import ua.mazik.delta.util.Color;
import ua.mazik.delta.util.Pixmap;
import ua.mazik.delta.util.TextureAtlas;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.function.Function;

public abstract class Renderer {
    private static Renderer renderer;

    public static void init(@NonNull API api) {
        if (Renderer.renderer != null) {
            throw new RuntimeException();
        }

        Renderer.renderer = switch (api) {
            case OpenGL -> GLRenderer.INSTANCE;
        };
    }

    public static @NonNull Renderer getInstance() {
        if (renderer == null) {
            throw new RuntimeException();
        }

        return renderer;
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

    public abstract void onWindowCreate(GLFWWindow window);

    public enum API {
        OpenGL
    }
}
