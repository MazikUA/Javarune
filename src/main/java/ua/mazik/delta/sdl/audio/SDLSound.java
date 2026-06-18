package ua.mazik.delta.sdl.audio;

import org.lwjgl.sdl.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public record SDLSound(ByteBuffer wavData, SDL_AudioSpec spec) implements AutoCloseable {

    @Override
    public void close() {
        MemoryUtil.memFree(this.wavData);
        this.spec.free();
    }
}
