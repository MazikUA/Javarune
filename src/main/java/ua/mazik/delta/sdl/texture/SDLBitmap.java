package ua.mazik.delta.sdl.texture;

import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.sdl.*;
import org.lwjgl.system.MemoryUtil;
import ua.mazik.delta.sdl.util.SDLUtil;
import ua.mazik.delta.spng.SPNGImage;
import ua.mazik.delta.util.PixelData;

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

        if (this.sdlSurface == null) {
            SDLUtil.throwException();
        }
    }

    public PixelData getRegion(int x, int y, int w, int h) {
        ByteBuffer region = BufferUtils.createByteBuffer(w * h * 4);

        for (int row = 0; row < h; row++) {
            int src = (y + row) * this.sdlSurface.pitch() + x * 4;
            int dst = row * w * 4;

            for (int col = 0; col < w * 4; col++) {
                region.put(dst + col, this.pixels.get(src + col));
            }
        }

        return new PixelData(w, h, region);
    }

    @Override
    public void close() {
        SDL_DestroySurface(this.sdlSurface);
        MemoryUtil.memFree(pixels);
    }
}
