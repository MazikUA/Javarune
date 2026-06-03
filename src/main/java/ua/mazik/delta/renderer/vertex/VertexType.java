package ua.mazik.delta.renderer.vertex;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL33;
import ua.mazik.delta.util.Pixel;

import java.nio.ByteBuffer;
import java.util.function.BiConsumer;

public record VertexType<T>(int glType, int size, int byteSize, boolean isInteger, BiConsumer<T, ByteBuffer> consumer) {
    public static final VertexType<Vector2f> VEC2 = new VertexType<>(GL33.GL_FLOAT, 2, Float.BYTES, false, (vec, buf) -> buf.putFloat(vec.x).putFloat(vec.y));
    public static final VertexType<Pixel> PIXEL = new VertexType<>(GL33.GL_FLOAT, 4, Float.BYTES, false, Pixel::put);
}
