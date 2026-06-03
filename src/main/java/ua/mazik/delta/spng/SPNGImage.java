package ua.mazik.delta.spng;

import org.joml.Vector2i;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.spng.spng_ihdr;
import ua.mazik.delta.util.LWJGLUtil;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

import static org.lwjgl.util.spng.SPNG.*;

/**
 * The {@link SPNGImage} represents image data loaded using SPNG library.
 */
public class SPNGImage implements AutoCloseable {
    public final long address;

    public SPNGImage() {
        this.address = spng_ctx_new(0);
    }

    /**
     * Creates new {@link SPNGImage} instance and runs {@link SPNGImage#setBuffer} with given buffer.
     *
     * @param buffer {@link ByteBuffer} with raw data.
     * @return {@link SPNGImage} instance.
     */
    public static SPNGImage fromBuffer(ByteBuffer buffer) {
        SPNGImage image = new SPNGImage();

        image.setBuffer(buffer);

        return image;
    }

    private static void check(int result) {
        if (result != 0) {
            throw new SPNGException(spng_strerror(result));
        }
    }

    /**
     * Sets SPNG byte buffer.
     *
     * @param buffer {@link ByteBuffer} with raw data.
     */
    public void setBuffer(ByteBuffer buffer) {
        check(spng_set_png_buffer(this.address, buffer));
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
     * Return pixels data using {@code spng_decode_image}.
     *
     * @return {@link ByteBuffer} with pixels data.
     * @apiNote Should be freed after usage using {@link MemoryUtil#memFree}.
     */
    public ByteBuffer pixels() {
        ByteBuffer buf = MemoryUtil.memAlloc((int) this.calculateSize());

        check(spng_decode_image(this.address, buf, SPNG_FMT_RGBA8, 0));

        return buf;
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
        spng_ctx_free(this.address);
    }
}
