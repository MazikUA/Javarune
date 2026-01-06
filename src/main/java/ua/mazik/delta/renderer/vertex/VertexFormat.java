package ua.mazik.delta.renderer.vertex;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL33;
import ua.mazik.delta.util.Pixel;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class VertexFormat<T> {
    public final Attribute<T, ?>[] attributes;
    public final int stride;

    @SafeVarargs
    public VertexFormat(Attribute<T, ?>... attributes) {
        this.attributes = attributes;
        this.stride = Arrays.stream(attributes)
                .mapToInt(attribute -> attribute.type.components * attribute.type.byteSize)
                .sum();
    }

    public record Attribute<T, R>(String name, Type<R> type, boolean normalized, Function<T, R> getter) {
        public Attribute(String name, Type<R> type, Function<T, R> getter) {
            this(name, type, false, getter);
        }
    }

    public record Type<T>(int glType, int components, int byteSize, boolean integer, BiConsumer<T, ByteBuffer> consumer) {
        public static final Type<Vector2f> VEC2 = new Type<>(GL33.GL_FLOAT, 2, Float.BYTES, false, (vec, buf) -> buf.putFloat(vec.x).putFloat(vec.y));
        public static final Type<Pixel> PIXEL = new Type<>(GL33.GL_FLOAT, 4, Float.BYTES, false, Pixel::put);
    }
}
