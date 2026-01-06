package ua.mazik.delta.renderer;

import org.lwjgl.BufferUtils;
import ua.mazik.delta.renderer.vertex.VertexFormat;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class VertexBuilder<T> {
    public final VertexFormat<T> format;

    private final List<T> vertices = new ArrayList<>();
    private final List<Integer> indices = new ArrayList<>();

    public VertexBuilder(VertexFormat<T> format) {
        this.format = format;
    }

    public ByteBuffer getVertices(ByteBuffer buffer) {
        for (T data : this.vertices) {
            for (VertexFormat.Attribute<T, Object> attribute : (VertexFormat.Attribute<T, Object>[]) format.attributes) {
                attribute.type().consumer().accept(attribute.getter().apply(data), buffer);
            }
        }
        return buffer;
    }

    public ByteBuffer getIndices(ByteBuffer buffer) {
        for (Integer index : this.indices) {
            buffer.putInt(index);
        }
        return buffer;
    }

    public ByteBuffer createVertexBuffer() {
        ByteBuffer buffer = BufferUtils.createByteBuffer(vertices.size() * format.stride);
        this.getVertices(buffer);
        buffer.flip();
        return buffer;
    }

    public ByteBuffer createIndexBuffer() {
        ByteBuffer buffer = BufferUtils.createByteBuffer(indices.size() * Integer.BYTES);
        this.getIndices(buffer);
        buffer.flip();
        return buffer;
    }

    public VertexBuilder<T> quad(T d1, T d2, T d3, T d4) {
        int nextIndex = vertices.size();

        this.vertices.add(d1);
        this.vertices.add(d2);
        this.vertices.add(d3);
        this.vertices.add(d4);

        this.indices.add(nextIndex);
        this.indices.add(nextIndex + 1);
        this.indices.add(nextIndex + 3);

        this.indices.add(nextIndex + 1);
        this.indices.add(nextIndex + 2);
        this.indices.add(nextIndex + 3);

        return this;
    }

    public int indexCount() {
        return this.indices.size();
    }
}
