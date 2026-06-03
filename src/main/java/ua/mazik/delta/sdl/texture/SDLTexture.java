package ua.mazik.delta.sdl.texture;

import org.lwjgl.sdl.*;
import ua.mazik.delta.sdl.renderer.SDLRenderer;
import ua.mazik.delta.sdl.util.SDLUtil;
import ua.mazik.delta.util.LWJGLUtil;

import static org.lwjgl.sdl.SDLRender.*;
import static org.lwjgl.sdl.SDLSurface.*;

public class SDLTexture implements AutoCloseable {
    public final SDL_Texture sdlTexture;
    public final SDLRenderer renderer;

    public SDLTexture(SDLBitmap bitmap, SDLRenderer renderer) {
        this.sdlTexture = SDL_CreateTextureFromSurface(renderer.address, bitmap.sdlSurface);

        if (this.sdlTexture == null) {
            SDLUtil.throwException();
        }

        this.renderer = renderer;

        SDL_SetTextureScaleMode(this.sdlTexture, SDL_SCALEMODE_NEAREST);
    }

    public void draw(float x, float y, float w, float h, float u, float v, float uw, float uh) {
        LWJGLUtil.withStack(SDL_FRect::malloc, srcRect -> {
            srcRect.set(u, v, uw, uh);

            LWJGLUtil.withStack(SDL_FRect::malloc, dstRect -> {
                dstRect.set(x, y, w, h);

                SDLUtil.check(SDL_RenderTexture(this.renderer.address, this.sdlTexture, srcRect, dstRect));
            });
        });
    }

    public void draw(float x, float y, float w, float h) {
        LWJGLUtil.withStack(SDL_FRect::malloc, dstRect -> {
            dstRect.set(x, y, w, h);

            SDLUtil.check(SDL_RenderTexture(this.renderer.address, this.sdlTexture, null, dstRect));
        });
    }

    @Override
    public void close() throws Exception {
        SDL_DestroyTexture(this.sdlTexture);
    }
}
