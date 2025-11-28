package ua.mazik.delta.renderer.attribute;

import java.nio.ByteBuffer;

public interface VertexAttribute {
    void apply(int index, int stride, long offset);

    void toBuffer(ByteBuffer buffer);

    int size();
}
