package ua.mazik.delta.sdl.texture;

import org.joml.Vector2i;
import org.lwjgl.sdl.*;
import org.lwjgl.system.MemoryStack;
import ua.mazik.delta.sdl.renderer.SDLRenderer;
import ua.mazik.delta.sdl.util.SDLUtil;
import ua.mazik.delta.spng.SPNGImage;
import ua.mazik.delta.util.LWJGLUtil;
import ua.mazik.delta.util.PixelData;

import java.nio.IntBuffer;

import static org.lwjgl.sdl.SDLPixels.*;
import static org.lwjgl.sdl.SDLRender.*;
import static org.lwjgl.sdl.SDLSurface.*;

public class SDLTexture implements AutoCloseable {
    private static final float PIXEL_CRAWL_OFFSET = -0.01f;

    public final SDL_Texture sdlTexture;
    public final SDLRenderer renderer;

    public SDLTexture(SPNGImage image, SDLRenderer renderer) {
        Vector2i resolution = image.resolution();

        SDL_Surface surface = SDL_CreateSurfaceFrom(resolution.x, resolution.y, SDL_PIXELFORMAT_RGBA32, image.pixels(), resolution.x * 4);

        if (surface == null) {
            SDLUtil.throwException();
        }

        this.sdlTexture = SDL_CreateTextureFromSurface(renderer.address, surface);

        if (this.sdlTexture == null) {
            SDLUtil.throwException();
        }

        this.renderer = renderer;

        SDLUtil.check(SDL_SetTextureScaleMode(this.sdlTexture, SDL_SCALEMODE_NEAREST));
    }

    public SDLTexture(int width, int height, Access access, SDLRenderer renderer) {
        this.sdlTexture = SDL_CreateTexture(renderer.address, SDL_PIXELFORMAT_RGBA32, access.ordinal(), width, height);

        if (this.sdlTexture == null) {
            SDLUtil.throwException();
        }

        this.renderer = renderer;

        SDLUtil.check(SDL_SetTextureScaleMode(this.sdlTexture, SDL_SCALEMODE_NEAREST));
    }

    public void draw(float x, float y, float w, float h, float u0, float v0, float u1, float v1) {
        LWJGLUtil.withStack(SDL_FRect::malloc, srcRect -> {
            srcRect.set(u0, v0, u1, v1);

            LWJGLUtil.withStack(SDL_FRect::malloc, dstRect -> {
                dstRect.set(x + PIXEL_CRAWL_OFFSET, y + PIXEL_CRAWL_OFFSET, w, h);

                SDLUtil.check(SDL_RenderTexture(this.renderer.address, this.sdlTexture, srcRect, dstRect));
            });
        });
    }

    public void draw(float x, float y, float w, float h) {
        LWJGLUtil.withStack(SDL_FRect::malloc, dstRect -> {
            dstRect.set(x + PIXEL_CRAWL_OFFSET, y + PIXEL_CRAWL_OFFSET, w, h);

            SDLUtil.check(SDL_RenderTexture(this.renderer.address, this.sdlTexture, null, dstRect));
        });
    }

    public void draw(float x, float y, int scale) {
        this.draw(x, y, this.sdlTexture.w() * scale, this.sdlTexture.h() * scale);
    }

    public void draw(float x, float y) {
        this.draw(x, y, 1);
    }

    public void drawQuad(SDLQuad quad) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            SDL_Vertex.Buffer buf = quad.buffer(stack);
            IntBuffer indices = stack.ints(0, 1, 2, 0, 2, 3);

            SDLUtil.check(SDL_RenderGeometry(this.renderer.address, this.sdlTexture, buf, indices));
        }
    }

    public void update(int x, int y, int w, int h, PixelData pixels) {
        LWJGLUtil.withStack(SDL_Rect::malloc, rect -> {
            rect.set(x, y, w, h);

            SDLUtil.check(SDL_UpdateTexture(this.sdlTexture, rect, pixels.buffer(), w * 4));
        });
    }

    @Override
    public void close() {
        SDL_DestroyTexture(this.sdlTexture);
    }

    public enum Access {
        STATIC,
        STREAMING,
        TARGET
    }
}
