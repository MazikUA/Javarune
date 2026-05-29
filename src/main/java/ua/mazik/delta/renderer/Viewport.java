package ua.mazik.delta.renderer;

import org.joml.Matrix4f;
import org.lwjgl.sdl.*;
import ua.mazik.delta.Renderer;
import ua.mazik.delta.util.SDLUtil;

import static org.lwjgl.sdl.SDLRender.*;

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
        SDLUtil.withStack(SDL_Rect::malloc, (rect) -> {
            rect.set(this.x, this.y, this.width, this.height);

            SDL_SetRenderViewport(Renderer.address, rect);
        });
    }

    public void enableScissors() {
        SDLUtil.withStack(SDL_Rect::malloc, (rect) -> {
            rect.set(this.x, this.y, this.width, this.height);

            SDL_SetRenderClipRect(Renderer.address, rect);
        });
    }

    public void disableScissors() {
        SDL_SetRenderClipRect(Renderer.address, null);
    }

    public void drawFillRect() {
        SDLUtil.withStack(SDL_FRect::malloc, (rect) -> {
            rect.set(0, 0, this.width, this.height);

            SDL_RenderFillRect(Renderer.address, rect);
        });
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

        Scaling FIT_PIXEL_PERFECT = (windowWidth, windowHeight, viewport) -> {
            int scale = Math.min(windowWidth / 320, windowHeight / 240);

            int scaledWidth = 320 * scale;
            int scaledHeight = 240 * scale;

            int scaledX = (windowWidth - scaledWidth) / 2;
            int scaledY = (windowHeight - scaledHeight) / 2;

            viewport.setSize(scaledWidth, scaledHeight);
            viewport.setPosition(scaledX, scaledY);
        };

        void resize(int windowWidth, int windowHeight, Viewport viewport);
    }
}
