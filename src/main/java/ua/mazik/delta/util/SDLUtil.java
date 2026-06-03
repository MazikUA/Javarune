package ua.mazik.delta.util;

import org.lwjgl.system.MemoryStack;

import java.util.function.Consumer;
import java.util.function.Function;

public class SDLUtil {
    public static <T> void withStack(Function<MemoryStack, T> constructor, Consumer<T> consumer) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            T obj = constructor.apply(stack);

            consumer.accept(obj);
        }
    }
}
