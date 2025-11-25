package ua.mazik.delta.renderer.gl;

import org.lwjgl.opengl.GL33;
import ua.mazik.delta.renderer.Texture;

public class GLTexture implements Texture {
    public final int id;

    protected GLTexture(int id) {
        this.id = id;
    }

    @Override
    public void close() {
        GL33.glDeleteTextures(id);
    }
}
