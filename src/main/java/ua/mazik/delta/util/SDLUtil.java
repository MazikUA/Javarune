package ua.mazik.delta.util;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.util.function.Consumer;
import java.util.function.Function;

public class SDLUtil {
    public static <T> void withStack(Function<MemoryStack, T> constructor, Consumer<T> consumer) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            T obj = constructor.apply(stack);

            consumer.accept(obj);
        }
    }

    public static ByteBuffer clone(ByteBuffer originalBuffer) {
        ByteBuffer clone = BufferUtils.createByteBuffer(originalBuffer.capacity());

        originalBuffer.rewind();
        clone.put(originalBuffer);
        originalBuffer.rewind();

        clone.flip();
        return clone;
    }
}
