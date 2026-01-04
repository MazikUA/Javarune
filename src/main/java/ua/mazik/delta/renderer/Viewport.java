package ua.mazik.delta.renderer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL33;

public class Viewport {
    public final Scaling scaling;
    public final Matrix4f projectionMatrix;
    private int width;
    private int height;
    private int x;
    private int y;

    public Viewport(int width, int height, Scaling scaling) {
        this.scaling = scaling;

        this.width = width;
        this.height = height;

        this.projectionMatrix = new Matrix4f().ortho(0, width, height, 0, -1, 1);
    }

    public void apply() {
        GL33.glViewport(this.x, this.y, this.width, this.height);
    }

    public void enableScissors() {
        GL33.glEnable(GL33.GL_SCISSOR_TEST);
        GL33.glScissor(this.x, this.y, this.width, this.height);
    }

    public void disableScissors() {
        GL33.glDisable(GL33.GL_SCISSOR_TEST);
    }

    public void resize(int windowWidth, int windowHeight) {
        this.scaling.resize(windowWidth, windowHeight, this);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    @FunctionalInterface
    public interface Scaling {
        Scaling FIT = (windowWidth, windowHeight, viewport) -> {
            if (windowWidth <= 0 || windowHeight <= 0) return;

            float scale = Math.min((float) windowWidth / viewport.width, (float) windowHeight / viewport.height);

            int scaledWidth = Math.round(viewport.width * scale);
            int scaledHeight = Math.round(viewport.height * scale);

            int scaledX = (windowWidth - scaledWidth) / 2;
            int scaledY = (windowHeight - scaledHeight) / 2;

            viewport.setSize(scaledWidth, scaledHeight);
            viewport.setPosition(scaledX, scaledY);
        };

        void resize(int windowWidth, int windowHeight, Viewport viewport);
    }
}
