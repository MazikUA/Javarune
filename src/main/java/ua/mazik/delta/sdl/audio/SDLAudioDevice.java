package ua.mazik.delta.sdl.audio;

import ua.mazik.delta.sdl.util.SDLUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.sdl.SDLAudio.*;

public class SDLAudioDevice implements AutoCloseable {
    public final int address;
    public final List<Long> activeStreams;

    public SDLAudioDevice() {
        this.address = SDL_OpenAudioDevice(SDL_AUDIO_DEVICE_DEFAULT_PLAYBACK, null);

        if (this.address == 0) {
            SDLUtil.throwException();
        }

        this.activeStreams = new ArrayList<>();
    }

    public void play(SDLSound sound) {
        long stream = SDLUtil.check(SDL_CreateAudioStream(sound.spec(), null));

        SDLUtil.check(SDL_BindAudioStream(this.address, stream));
        SDLUtil.check(SDL_PutAudioStreamData(stream, sound.wavData()));
        SDLUtil.check(SDL_FlushAudioStream(stream));

        this.activeStreams.add(stream);
    }

    public void update() {
        Iterator<Long> iterator = this.activeStreams.iterator();

        while (iterator.hasNext()) {
            long stream = iterator.next();

            if (SDL_GetAudioStreamQueued(stream) == 0) {
                SDL_UnbindAudioStream(stream);
                SDL_DestroyAudioStream(stream);

                iterator.remove();
            }
        }
    }

    @Override
    public void close() {
        for (long stream : this.activeStreams) {
            SDL_UnbindAudioStream(stream);
            SDL_DestroyAudioStream(stream);
        }
        this.activeStreams.clear();

        SDL_CloseAudioDevice(this.address);
    }
}
