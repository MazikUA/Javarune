package ua.mazik.delta.renderer.gl;

import org.lwjgl.opengl.GL33;
import ua.mazik.delta.renderer.Viewport;

public class GLViewport extends Viewport {
    protected GLViewport(int width, int height, Scaling scaling) {
        super(width, height, scaling, new GLCamera(width, height));
    }

    @Override
    public void enableScissors() {
        GL33.glEnable(GL33.GL_SCISSOR_TEST);
        GL33.glScissor(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void disableScissors() {
        GL33.glDisable(GL33.GL_SCISSOR_TEST);
    }

    @Override
    public void apply() {
        GL33.glViewport(this.getX(), this.getY(), this.getWidth(), this.getHeight());

        super.apply();
    }
}
