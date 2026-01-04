package ua.mazik.javarune.asset.loader;

import org.jspecify.annotations.NonNull;
import org.lwjgl.openal.AL10;
import ua.mazik.delta.audio.Sound;
import ua.mazik.javarune.asset.AssetLoader;
import ua.mazik.javarune.asset.AssetManager;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SoundLoader extends AssetLoader<Optional<Sound>> {
    private final Map<String, Sound> sounds = new HashMap<>();

    public SoundLoader(@NonNull AssetManager assetManager) {
        super("sounds", assetManager);
    }

    @Override
    public void load() {
        this.close();
    }

    @Override
    public @NonNull Optional<Sound> get(@NonNull String path) {
        Sound sound = this.sounds.get(path);

        if (sound == null) {
            Optional<InputStream> asset = this.assetManager.findAsset("sounds/" + path + ".wav");

            if (asset.isPresent()) {
                try (InputStream in = asset.get()) {
                    AudioInputStream ais = AudioSystem.getAudioInputStream(in);
                    AudioFormat audioFormat = ais.getFormat();

                    int format = audioFormat.getChannels() == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16;
                    byte[] bytes = ais.readAllBytes();

                    ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length).order(ByteOrder.nativeOrder());
                    buffer.put(bytes).flip();

                    Sound newSound = new Sound(format, buffer, audioFormat);

                    this.sounds.put(path, newSound);
                    return Optional.of(newSound);
                } catch (IOException | UnsupportedAudioFileException e) {
                    e.printStackTrace();
                }
            }

            return Optional.empty();
        } else {
            return Optional.of(sound);
        }
    }

    @Override
    public void close() {
        this.sounds.values().forEach(Sound::close);
        this.sounds.clear();
    }
}
