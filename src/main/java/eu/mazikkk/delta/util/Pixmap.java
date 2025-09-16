package eu.mazikkk.delta.util;

import eu.mazikkk.delta.Renderer;
import eu.mazikkk.delta.Texture;
import org.jspecify.annotations.NonNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Pixmap implements AutoCloseable {
    public final int width;
    public final int height;

    private final ByteBuffer rawData;
    private final boolean stbImage;

    private Pixmap(int width, int height, ByteBuffer rawData, boolean stbImage) {
        this.width = width;
        this.height = height;

        this.rawData = rawData;
        this.stbImage = stbImage;
    }

    public Pixmap(int width, int height) {
        this(width, height, BufferUtils.createByteBuffer(width * height * 4), false);
    }

    public static @NonNull Pixmap fromRawBytes(int width, int height, byte[] rawBytes) {
        ByteBuffer rawData = BufferUtils.createByteBuffer(rawBytes.length);

        rawData.put(rawBytes);
        rawData.flip();

        return new Pixmap(width, height, rawData, false);
    }

    public static @NonNull Pixmap fromImage(@NonNull ByteBuffer imageBuffer) {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer components = BufferUtils.createIntBuffer(1);

        // STBImage.stbi_set_flip_vertically_on_load(true);
        ByteBuffer rawData = STBImage.stbi_load_from_memory(imageBuffer, width, height, components, 4);

        if (rawData == null) {
            throw new IllegalArgumentException("Buffer is corrupted: " + STBImage.stbi_failure_reason());
        }

        return new Pixmap(width.get(), height.get(), rawData, true);
    }

    public static @NonNull Pixmap fromImageBytes(byte[] bytes) {
        ByteBuffer imageBuffer = BufferUtils.createByteBuffer(bytes.length);

        imageBuffer.put(bytes);
        imageBuffer.flip();

        return fromImage(imageBuffer);
    }

    public @NonNull Color getPixel(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            throw new IllegalArgumentException("Pixel out of bounds: (" + x + "," + y + ")");
        }

        int offset = (y * width + x) * 4;

        int r = this.rawData.get(offset) & 0xFF;
        int g = this.rawData.get(offset + 1) & 0xFF;
        int b = this.rawData.get(offset + 2) & 0xFF;
        int a = this.rawData.get(offset + 3) & 0xFF;

        return new Color(r, g, b, a);
    }

    public void setPixel(int x, int y, @NonNull Color color) {
        if (!this.inBounds(x, y)) {
            throw new IllegalArgumentException("Pixel out of bounds: (" + x + "," + y + ")");
        }

        int offset = (y * width + x) * 4;

        rawData.put(offset, (byte) color.red);
        rawData.put(offset + 1, (byte) color.green);
        rawData.put(offset + 2, (byte) color.blue);
        rawData.put(offset + 3, (byte) color.alpha);
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
                    Color color = pixmap.getPixel(srcX, srcY);

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

    public @NonNull Texture toTexture(@NonNull Renderer renderer) {
        return renderer.createTexture(this.width, this.height, this.getBuffer());
    }

    @Override
    public void close() {
        if (this.stbImage) {
            STBImage.stbi_image_free(this.rawData);
        }
    }
}
