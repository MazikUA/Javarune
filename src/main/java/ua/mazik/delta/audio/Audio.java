package ua.mazik.delta.audio;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Audio {
    private static long device;
    private static long context;

    public static void init() {
        device = ALC10.alcOpenDevice((ByteBuffer) null);

        if (device == 0) {
            throw new RuntimeException();
        }

        context = ALC10.alcCreateContext(device, (IntBuffer) null);
        ALC10.alcMakeContextCurrent(context);

        AL.createCapabilities(ALC.createCapabilities(device));
    }

    public static void shutdown() {
        ALC10.alcDestroyContext(context);
        ALC10.alcCloseDevice(device);
    }
}
