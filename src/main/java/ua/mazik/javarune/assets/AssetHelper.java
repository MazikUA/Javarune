package ua.mazik.javarune.assets;

import ua.mazik.delta.sdl.texture.SDLTexture;
import ua.mazik.delta.util.Lazy;
import ua.mazik.javarune.Javarune;

public final class AssetHelper {
    private static final Lazy<SDLTexture> BROKEN_TEXTURE = new Lazy<>(() -> Javarune.textureLoader().get("misc/asgore").orElseThrow());

    private AssetHelper() {
    }

    public static void playSound(String path) {
        Javarune.soundLoader().get(path).ifPresent(Javarune.audioDevice()::play);
    }

    public static SDLTexture texture(String path) {
        return Javarune.textureLoader().get(path).orElseGet(AssetHelper::brokenTexture);
    }

    public static SDLTexture brokenTexture() {
        return BROKEN_TEXTURE.get();
    }
}
