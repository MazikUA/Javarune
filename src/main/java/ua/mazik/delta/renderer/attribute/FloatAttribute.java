package ua.mazik.delta.renderer.attribute;

import org.lwjgl.opengl.GL33;

import java.nio.ByteBuffer;

public record FloatAttribute(float... floats) implements VertexAttribute {
    @Override
    public void apply(int index, int stride, long offset) {
        GL33.glVertexAttribPointer(index, floats.length, GL33.GL_FLOAT, false, stride, offset);
        GL33.glEnableVertexAttribArray(index);
    }

    @Override
    public void toBuffer(ByteBuffer buffer) {
        for (float value : floats) {
            buffer.putFloat(value);
        }
    }

    @Override
    public int size() {
        return floats.length * Float.BYTES;
    }
}
