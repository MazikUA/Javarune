package eu.mazikkk.delta.gl;

import eu.mazikkk.delta.Texture;
import org.lwjgl.opengl.GL33;

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
