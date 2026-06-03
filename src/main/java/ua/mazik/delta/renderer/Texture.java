package ua.mazik.delta.renderer;

import org.jspecify.annotations.NonNull;
import org.lwjgl.opengl.GL33;
import ua.mazik.delta.util.Bindable;
import ua.mazik.delta.util.Pixmap;

import java.nio.ByteBuffer;

public class Texture implements Bindable, AutoCloseable {
    public final int id;

    public final int width;
    public final int height;

    public Texture(int width, int height, ByteBuffer buffer) {
        this.id = GL33.glGenTextures();

        this.bind();

        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_NEAREST);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_NEAREST);
        GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGBA8, width, height, 0, GL33.GL_RGBA, GL33.GL_UNSIGNED_BYTE, buffer);

        this.unbind();

        this.width = width;
        this.height = height;
    }

    public void subImage(@NonNull Pixmap pixmap, int x, int y, int width, int height) {
        this.bind();

        GL33.glTexSubImage2D(GL33.GL_TEXTURE_2D, 0, x, y, width, height, GL33.GL_RGBA, GL33.GL_UNSIGNED_BYTE, pixmap.getBuffer());

        this.unbind();
    }

    @Override
    public void bind() {
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, this.id);
    }

    @Override
    public void unbind() {
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, 0);
    }

    @Override
    public void close() {
        GL33.glDeleteTextures(this.id);
    }
}
