package ua.mazik.delta.audio;

import org.lwjgl.sdl.*;

import javax.sound.sampled.AudioFormat;
import java.nio.ByteBuffer;

import static org.lwjgl.sdl.SDLAudio.*;

public class Sound implements AutoCloseable {
    public final ByteBuffer buffer;
    public final long stream;

    public Sound(ByteBuffer buffer, AudioFormat audioFormat) {
        this.buffer = buffer;

        SDL_AudioSpec spec = SDL_AudioSpec.create()
                .format(SDL_AUDIO_S16)
                .channels(audioFormat.getChannels())
                .freq((int) audioFormat.getSampleRate());

        this.stream = SDL_OpenAudioDeviceStream(SDL_AUDIO_DEVICE_DEFAULT_PLAYBACK, spec, null, 0);

        SDL_ResumeAudioStreamDevice(this.stream);

        spec.close();
    }

    public void play() {
        SDL_ClearAudioStream(this.stream);

        SDL_PutAudioStreamData(this.stream, this.buffer);
    }

    @Override
    public void close() {
        SDL_DestroyAudioStream(this.stream);
    }
}
