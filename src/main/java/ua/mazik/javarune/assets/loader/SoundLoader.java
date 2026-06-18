package ua.mazik.javarune.assets.loader;

import org.lwjgl.PointerBuffer;
import org.lwjgl.sdl.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import ua.mazik.delta.assets.Asset;
import ua.mazik.delta.assets.AssetSource;
import ua.mazik.delta.assets.CacheableAssetLoader;
import ua.mazik.delta.sdl.audio.SDLSound;
import ua.mazik.delta.sdl.util.SDLUtil;
import ua.mazik.delta.util.LWJGLUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Optional;

import static org.lwjgl.sdl.SDLAudio.*;
import static org.lwjgl.sdl.SDLIOStream.*;
import static org.lwjgl.sdl.SDLStdinc.*;

public class SoundLoader extends CacheableAssetLoader<SDLSound> {
    public SoundLoader(AssetSource assetSource) {
        super(assetSource);
    }

    @Override
    public Optional<SDLSound> load(String path) {
        Optional<Asset> asset = this.assetSource.getAsset("sounds/" + path + ".wav");

        if (asset.isPresent()) {
            ByteBuffer bytes = LWJGLUtil.createBuffer(asset.get().getBytes());

            long io = SDLUtil.check(SDL_IOFromConstMem(bytes));

            SDL_AudioSpec spec = SDL_AudioSpec.malloc();

            try (MemoryStack stack = MemoryStack.stackPush()) {
                PointerBuffer audioBuf = stack.mallocPointer(1);
                IntBuffer audioLen = stack.mallocInt(1);

                SDLUtil.check(SDL_LoadWAV_IO(io, true, spec, audioBuf, audioLen));

                long wavPtr = audioBuf.get(0);
                int wavLen = audioLen.get(0);

                ByteBuffer wavData = MemoryUtil.memAlloc(wavLen);
                MemoryUtil.memCopy(wavPtr, MemoryUtil.memAddress(wavData), wavLen);

                nSDL_free(wavPtr);

                return Optional.of(new SDLSound(wavData, spec));
            }
        }

        return Optional.empty();
    }
}
