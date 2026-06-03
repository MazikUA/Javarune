package ua.mazik.delta.sdl.texture;

import org.joml.Vector2i;
import org.lwjgl.sdl.*;
import org.lwjgl.system.MemoryUtil;
import ua.mazik.delta.spng.SPNGImage;

import java.nio.ByteBuffer;

import static org.lwjgl.sdl.SDLPixels.*;
import static org.lwjgl.sdl.SDLSurface.*;

public class SDLBitmap implements AutoCloseable {
    public final SDL_Surface sdlSurface;
    private final ByteBuffer pixels;

    public SDLBitmap(SPNGImage spng) {
        Vector2i resolution = spng.resolution();

        this.pixels = spng.pixels();
        this.sdlSurface = SDL_CreateSurfaceFrom(resolution.x, resolution.y, SDL_PIXELFORMAT_RGBA32, pixels, resolution.x * 4);
    }

    @Override
    public void close() {
        SDL_DestroySurface(this.sdlSurface);
        MemoryUtil.memFree(pixels);
    }
}
