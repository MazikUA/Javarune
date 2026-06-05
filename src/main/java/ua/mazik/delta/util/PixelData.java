package ua.mazik.delta.util;

import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public record PixelData(int width, int height, ByteBuffer buffer) implements AutoCloseable {
    @Override
    public void close() {
        MemoryUtil.memFree(this.buffer);
    }
}
