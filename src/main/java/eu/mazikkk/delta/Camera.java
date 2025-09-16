package eu.mazikkk.delta;

import org.joml.Matrix4f;
import org.jspecify.annotations.NonNull;

public abstract class Camera {
    private int worldWidth;
    private int worldHeight;

    public Camera(int worldWidth, int worldHeight) {
        this.setSize(worldWidth, worldHeight);
    }

    public abstract void apply();

    public abstract @NonNull Matrix4f getProjectionMatrix();

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
