package ua.mazik.delta.util;

import org.jspecify.annotations.NonNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.sdl.*;
import ua.mazik.delta.renderer.Texture;

import java.nio.ByteBuffer;

import static org.lwjgl.sdl.SDLIOStream.*;
import static org.lwjgl.sdl.SDLSurface.*;

public class Pixmap {
    public final int width;
    public final int height;

    private final ByteBuffer rawData;

    private Pixmap(int width, int height, ByteBuffer rawData) {
        this.width = width;
        this.height = height;

        this.rawData = rawData;
    }

    public Pixmap(int width, int height) {
        this(width, height, BufferUtils.createByteBuffer(width * height * 4));
    }

    public static @NonNull Pixmap fromRawBytes(int width, int height, byte[] rawBytes) {
        ByteBuffer rawData = BufferUtils.createByteBuffer(rawBytes.length);

        rawData.put(rawBytes);
        rawData.flip();

        return new Pixmap(width, height, rawData);
    }

    public static @NonNull Pixmap fromImage(@NonNull ByteBuffer imageBuffer) {
        long io = SDL_IOFromConstMem(imageBuffer);

        if (io == 0) {
            throw new RuntimeException();
        }

        SDL_Surface surface = SDL_LoadSurface_IO(io, true);

        if (surface == null) {
            throw new RuntimeException();
        }

        int width = surface.w();
        int height = surface.h();

        ByteBuffer rawData = SDLUtil.clone(surface.pixels());

        SDL_DestroySurface(surface);

        if (rawData == null) {
            throw new IllegalArgumentException("Buffer is corrupted: ");
        }

        return new Pixmap(width, height, rawData);
    }

    public static @NonNull Pixmap fromImageBytes(byte[] bytes) {
        ByteBuffer imageBuffer = BufferUtils.createByteBuffer(bytes.length);

        imageBuffer.put(bytes);
        imageBuffer.flip();

        return fromImage(imageBuffer);
    }

    public @NonNull Pixel getPixel(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            throw new IllegalArgumentException("Pixel out of bounds: (" + x + "," + y + ")");
        }

        int offset = (y * width + x) * 4;

        int r = this.rawData.get(offset) & 0xFF;
        int g = this.rawData.get(offset + 1) & 0xFF;
        int b = this.rawData.get(offset + 2) & 0xFF;
        int a = this.rawData.get(offset + 3) & 0xFF;

        return new Pixel(r, g, b, a);
    }

    public void setPixel(int x, int y, @NonNull Pixel color) {
        if (!this.inBounds(x, y)) {
            throw new IllegalArgumentException("Pixel out of bounds: (" + x + "," + y + ")");
        }

        int offset = (y * width + x) * 4;

        rawData.put(offset, (byte) color.red());
        rawData.put(offset + 1, (byte) color.green());
        rawData.put(offset + 2, (byte) color.blue());
        rawData.put(offset + 3, (byte) color.alpha());
    }

    public void drawPixmap(int x, int y, @NonNull Pixmap pixmap) {
        this.drawPixmap(x, y, 0, 0, pixmap.width, pixmap.height, pixmap);
    }

    public void drawPixmap(int x, int y, int u, int v, int width, int height, @NonNull Pixmap pixmap) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int srcX = u + i;
                int srcY = v + j;

                int dstX = x + i;
                int dstY = y + j;

                if (this.inBounds(dstX, dstY)) {
                    Pixel color = pixmap.getPixel(srcX, srcY);

                    this.setPixel(dstX, dstY, color);
                }
            }
        }
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < this.width && y < this.height;
    }

    public @NonNull ByteBuffer getBuffer() {
        ByteBuffer duplicate = this.rawData.duplicate();

        duplicate.position(0);
        duplicate.limit(this.width * this.height * 4);

        return duplicate.asReadOnlyBuffer();
    }

    public @NonNull Texture toTexture() {
        return new Texture(this.width, this.height, this.getBuffer());
    }
}
