package ua.mazik.delta.renderer;

import org.lwjgl.BufferUtils;
import ua.mazik.delta.renderer.attribute.VertexAttribute;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class VertexBuilder {
    public final List<VertexAttribute[]> vertices = new ArrayList<>();
    public final List<Integer> indices = new ArrayList<>();

    private int stride = 0;
    private int indexOffset = 0;

    public void vertex(VertexAttribute... attributes) {
        this.vertices.add(attributes);

        if (this.vertices.size() == 1) {
            for (VertexAttribute attribute : attributes) {
                this.stride += attribute.size();
            }
        }
    }

    public void triangle(int i0, int i1, int i2) {
        this.indices.addAll(List.of(indexOffset + i0, indexOffset + i1, indexOffset + i2));
    }

    public ByteBuffer getVertexBuffer() {
        ByteBuffer buffer = BufferUtils.createByteBuffer(this.vertices.size() * this.stride);

        for (VertexAttribute[] attributePack : this.vertices) {
            for (VertexAttribute attribute : attributePack) {
                attribute.toBuffer(buffer);
            }
        }

        buffer.flip();

        return buffer;
    }

    public IntBuffer getIndexBuffer() {
        IntBuffer buffer = BufferUtils.createIntBuffer(this.indices.size());

        for (int index : this.indices) {
            buffer.put(index);
        }

        buffer.flip();

        return buffer;
    }

    public void enableVertexAttributes() {
        long offset = 0;
        int index = 0;

        for (VertexAttribute attribute : this.vertices.get(0)) {
            attribute.apply(index, this.stride, offset);

            offset += attribute.size();
            index++;
        }
    }

    public void nextOffset(int offset) {
        this.indexOffset += offset;
    }

    public void clear() {
        this.vertices.clear();
        this.indices.clear();

        this.stride = 0;
        this.indexOffset = 0;
    }
}
