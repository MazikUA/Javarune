package ua.mazik.delta.util;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Utility class for simplified usage of different LWJGL functions.
 */
public final class LWJGLUtil {
    private LWJGLUtil() {
    }

    /**
     * Pushes {@link MemoryStack}, creates {@link T} and consumes it with given consumer.
     * <br/>
     * <br/>
     * Example usage:
     * <pre>{@code
     * LWJGLUtil.withStack(SDL_FRect::malloc, rect -> {
     *     rect.set(0, 0, 640, 480);
     *     SDL_RenderFillRect(renderer, rect);
     * });
     * }</pre>
     *
     * @param function Function for creating {@link T} instance with {@link MemoryStack}.
     * @param consumer Consumer, which will consume {@link T} after creating.
     * @param <T>      Return type.
     */
    public static <T> void withStack(Function<MemoryStack, T> function, Consumer<T> consumer) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            T obj = function.apply(stack);
            consumer.accept(obj);
        }
    }

    /**
     * Creates new direct {@link ByteBuffer} and puts {@code bytes}.
     *
     * @param bytes Raw {@code byte[]} data.
     * @return Direct {@link ByteBuffer} with raw data.
     */
    public static ByteBuffer createBuffer(byte[] bytes) {
        ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);

        buffer.put(bytes);
        buffer.flip();

        return buffer;
    }
}
