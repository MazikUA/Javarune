package ua.mazik.delta.audio;

import org.lwjgl.sdl.*;

import static org.lwjgl.sdl.SDLAudio.*;

public class Audio {
    public static int audioDevice;

    public static void init() {
        audioDevice = SDL_OpenAudioDevice(SDL_AUDIO_DEVICE_DEFAULT_PLAYBACK, SDL_AudioSpec.create());
    }

    public static void shutdown() {
        SDL_CloseAudioDevice(audioDevice);
    }
}
