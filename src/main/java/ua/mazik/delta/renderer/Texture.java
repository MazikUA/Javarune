package ua.mazik.delta.renderer;

import org.jspecify.annotations.NonNull;
import org.lwjgl.sdl.*;
import ua.mazik.delta.util.Pixmap;
import ua.mazik.delta.util.SDLUtil;

import java.nio.ByteBuffer;

import static org.lwjgl.sdl.SDLPixels.*;
import static org.lwjgl.sdl.SDLRender.*;
import static org.lwjgl.sdl.SDLSurface.*;

public class Texture implements AutoCloseable {
    public final int width;
    public final int height;

    public final SDL_Texture sdlTexture;

    public Texture(int width, int height, ByteBuffer buffer) {
        this.width = width;
        this.height = height;

        SDL_Surface surface = SDL_CreateSurfaceFrom(width, height, SDL_PIXELFORMAT_RGBA32, buffer, width * 4);

        if (surface == null) {
            throw new RuntimeException();
        }

        this.sdlTexture = SDL_CreateTextureFromSurface(Renderer.address, surface);

        if (this.sdlTexture == null) {
            throw new RuntimeException();
        }

        SDL_SetTextureScaleMode(this.sdlTexture, SDL_SCALEMODE_PIXELART);
        SDL_DestroySurface(surface);
    }

    public void subImage(@NonNull Pixmap pixmap, int x, int y, int width, int height) {
        SDLUtil.withStack(SDL_Rect::malloc, (rect) -> {
            rect.set(x, y, width, height);

            SDL_UpdateTexture(this.sdlTexture, rect, pixmap.getBuffer(), width * 4);
        });
    }

    @Override
    public void close() {
        SDL_DestroyTexture(this.sdlTexture);
    }
}
