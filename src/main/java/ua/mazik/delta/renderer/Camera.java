package ua.mazik.delta.renderer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL33;

public class Camera {
    public final Matrix4f projectionMatrix;

    private int worldWidth;
    private int worldHeight;

    public Camera(int worldWidth, int worldHeight) {
        this.setSize(worldWidth, worldHeight);

        this.projectionMatrix = new Matrix4f().ortho(0, worldWidth, worldHeight, 0, -1, 1);
    }

    public void apply() {
        GL33.glMatrixMode(GL33.GL_PROJECTION);
        GL33.glLoadIdentity();
        GL33.glOrtho(0, this.getWorldWidth(), this.getWorldHeight(), 0, -1, 1);
        GL33.glMatrixMode(GL33.GL_MODELVIEW);
        GL33.glLoadIdentity();
    }

    public void setSize(int worldWidth, int worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public int getWorldWidth() {
        return this.worldWidth;
    }

    public int getWorldHeight() {
        return this.worldHeight;
    }
}
