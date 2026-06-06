package ua.mazik.delta.spng;

import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.spng.spng_ihdr;
import ua.mazik.delta.util.LWJGLUtil;
import ua.mazik.delta.util.PixelData;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

import static org.lwjgl.util.spng.SPNG.*;

/**
 * The {@link SPNGImage} represents image data loaded using SPNG library.
 */
public class SPNGImage implements AutoCloseable {
    public final long address;

    private ByteBuffer pixels;

    public SPNGImage(ByteBuffer buffer) {
        this.address = spng_ctx_new(0);

        check(spng_set_png_buffer(this.address, buffer));
    }

    private static void check(int result) {
        if (result != 0) {
            throw new SPNGException(spng_strerror(result));
        }
    }

    /**
     * Calculates image size using {@code spng_decoded_image_size}.
     *
     * @return Calculates size.
     */
    public long calculateSize() {
        AtomicLong imgSize = new AtomicLong();

        LWJGLUtil.withStack(stack -> stack.mallocPointer(1), pointer -> {
            check(spng_decoded_image_size(this.address, SPNG_FMT_RGBA8, pointer));

            imgSize.set(pointer.get(0));
        });

        return imgSize.get();
    }

    /**
     * Return buffer data using {@code spng_decode_image}.
     *
     * @return {@link ByteBuffer} with buffer data.
     * @apiNote Should be freed after usage using {@link MemoryUtil#memFree}.
     */
    public ByteBuffer pixels() {
        if (this.pixels != null) {
            return this.pixels;
        }

        ByteBuffer buf = MemoryUtil.memAlloc((int) this.calculateSize());

        check(spng_decode_image(this.address, buf, SPNG_FMT_RGBA8, 0));

        this.pixels = buf;

        return buf;
    }

    public PixelData getRegion(int x, int y, int w, int h) {
        int imgW = this.resolution().x;

        ByteBuffer pixels = this.pixels();
        ByteBuffer region = BufferUtils.createByteBuffer(w * h * 4);

        for (int row = 0; row < h; row++) {
            int src = ((y + row) * imgW + x) * 4;
            int dst = row * w * 4;

            for (int col = 0; col < w * 4; col++) {
                region.put(dst + col, pixels.get(src + col));
            }
        }

        return new PixelData(w, h, region);
    }

    /**
     *
     * @return {@link Vector2i} with image width ({@code x}) and height ({@code y}).
     */
    public Vector2i resolution() {
        Vector2i res = new Vector2i();

        LWJGLUtil.withStack(spng_ihdr::malloc, ihdr -> {
            spng_get_ihdr(this.address, ihdr);

            res.set(ihdr.width(), ihdr.height());
        });

        return res;
    }

    /**
     * Frees image memory.
     */
    @Override
    public void close() {
        if (this.pixels != null) {
            MemoryUtil.memFree(this.pixels);
        }

        spng_ctx_free(this.address);
    }
}
