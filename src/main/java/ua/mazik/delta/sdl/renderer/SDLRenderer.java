package ua.mazik.delta.sdl.renderer;

import org.lwjgl.sdl.*;
import ua.mazik.delta.sdl.util.SDLUtil;
import ua.mazik.delta.sdl.window.SDLWindow;
import ua.mazik.delta.util.LWJGLUtil;
import ua.mazik.delta.util.Pixel;

import static org.lwjgl.sdl.SDLRender.*;

/**
 * The {@link SDLRenderer} class is designed for simple usage of SDL Render API,
 * but for more complex actions prefer to use {@link org.lwjgl.sdl.SDLRender} class.
 */
public class SDLRenderer implements AutoCloseable {
    public final SDLWindow window;
    public final SDLDriver driver;

    public final long address;

    public SDLRenderer(SDLWindow window, SDLDriver driver) {
        this.window = window;
        this.driver = driver;

        this.address = SDLUtil.check(SDL_CreateRenderer(window.address, driver.name()));
    }

    /**
     * Enables or disables Vsync based on {@code value}.
     *
     * @param value Is Vsync should be enabled.
     */
    public void useVsync(boolean value) {
        SDLUtil.check(SDL_SetRenderVSync(this.address, value ? 1 : 0));
    }

    /**
     * Enables Vsync.
     */
    public void useVsync() {
        this.useVsync(true);
    }

    /**
     * Clears entire screen, ignoring viewport and clip rectangle.
     */
    public void clear() {
        SDLUtil.check(SDL_RenderClear(this.address));
    }

    /**
     * Updates screen with any performed rendering since the previous call.
     */
    public void present() {
        SDLUtil.check(SDL_RenderPresent(this.address));
    }

    /**
     * Sets render draw color for drawing operations.
     *
     * @param pixel Color.
     */
    public void setDrawColor(Pixel pixel) {
        SDLUtil.check(SDL_SetRenderDrawColor(
            this.address,
            (byte) pixel.red(),
            (byte) pixel.green(),
            (byte) pixel.blue(),
            (byte) pixel.alpha()
        ));
    }

    /**
     * Draws fill rect on screen.
     *
     * @param rect {@link SDL_FRect} instance.
     */
    public void drawFillRect(SDL_FRect rect) {
        SDLUtil.check(SDL_RenderFillRect(this.address, rect));
    }

    /**
     * Draws fill rect on screen using given params.
     *
     * @param x Rect horizontal position.
     * @param y Rect vertical position.
     * @param w Rect width.
     * @param h Rect height.
     */
    public void drawFillRect(float x, float y, float w, float h) {
        LWJGLUtil.withStack(SDL_FRect::malloc, rect -> {
            rect.set(x, y, w, h);
            this.drawFillRect(rect);
        });
    }

    /**
     * Sets SDL render scale. Recommended to use values with no floating point.
     * <br/>
     * <br/>
     * For example: if {@code scaleX} and {@code scaleY} equals {@code 0.5f},
     * and you try to render rectangle using {@link org.lwjgl.sdl.SDL_FRect} with {@code 50, 50, 320, 240} values,
     * this values will be converted to {@code 25, 25, 160, 120}.
     *
     * @param scaleX Horizontal scaling factor.
     * @param scaleY Vertical scaling factor.
     */
    public void setRenderScale(float scaleX, float scaleY) {
        SDLUtil.check(SDL_SetRenderScale(this.address, scaleX, scaleY));
    }

    /**
     * Sets SDL render logical presentation.
     *
     * @param width            Width of logical resolution.
     * @param height           Height of logical resolution.
     * @param presentationMode Presentation mode used.
     */
    public void setRenderLogicalPresentation(int width, int height, SDLPresentationMode presentationMode) {
        SDLUtil.check(SDL_SetRenderLogicalPresentation(this.address, width, height, presentationMode.ordinal()));
    }

    @Override
    public void close() {
        SDL_DestroyRenderer(this.address);
    }
}
