package eu.mazikkk.delta.gl;

import eu.mazikkk.delta.Camera;
import org.joml.Matrix4f;
import org.jspecify.annotations.NonNull;
import org.lwjgl.opengl.GL33;

public class GLCamera extends Camera {
    private final Matrix4f projectionMatrix;

    protected GLCamera(int worldWidth, int worldHeight) {
        super(worldWidth, worldHeight);

        this.projectionMatrix = new Matrix4f().ortho(0, worldWidth, worldHeight, 0, -1, 1);
    }

    @Override
    public void apply() {
        GL33.glMatrixMode(GL33.GL_PROJECTION);
        GL33.glLoadIdentity();
        GL33.glOrtho(0, this.getWorldWidth(), this.getWorldHeight(), 0, -1, 1);
        GL33.glMatrixMode(GL33.GL_MODELVIEW);
        GL33.glLoadIdentity();
    }

    @Override
    public @NonNull Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }
}
