package ua.mazik.delta.audio;

import org.lwjgl.openal.AL10;

import javax.sound.sampled.AudioFormat;
import java.nio.ByteBuffer;

public class Sound implements AutoCloseable {
    public final int id;
    public final int source;

    public Sound(int alFormat, ByteBuffer buffer, AudioFormat audioFormat) {
        this.id = AL10.alGenBuffers();

        AL10.alBufferData(this.id, alFormat, buffer, Math.round(audioFormat.getSampleRate()));

        this.source = AL10.alGenSources();
    }

    public void play() {
        AL10.alSourcei(this.source, AL10.AL_BUFFER, this.id);
        AL10.alSourcePlay(this.source);
    }

    @Override
    public void close() {
        AL10.alDeleteSources(this.source);
        AL10.alDeleteBuffers(this.id);
    }
}
