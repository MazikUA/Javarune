package ua.mazik.delta.fs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * The {@link DeltaFile} class is designed for simple usage of files,
 * but for more complex actions prefer to use {@link java.nio} package.
 *
 * @param path   File path.
 * @param folder The {@link DeltaFolder} instance, where the file located.
 */
public record DeltaFile(Path path, DeltaFolder folder) {
    /**
     * Reads file content.
     *
     * @return {@link Optional} with file content as {@code byte[]}. If file doesn't exist or there's an exception threw, empty {@link Optional} is returned.
     */
    public Optional<byte[]> read() {
        if (Files.exists(this.path)) {
            try {
                return Optional.of(Files.readAllBytes(this.path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Optional.empty();
    }

    /**
     * Reads file content.
     *
     * @return {@link Optional} with file content as {@link String}. If file doesn't exist or there's an exception threw, empty {@link Optional} is returned.
     */
    public Optional<String> readAsString() {
        Optional<byte[]> bytes = this.read();

        return bytes.map(value -> new String(value, StandardCharsets.UTF_8));
    }

    /**
     * Writes content to file.
     *
     * @param data Data to write in {@code byte[]} format.
     * @return The {@link DeltaFile} instance.
     */
    public DeltaFile write(byte[] data) {
        try {
            Path parent = this.path.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.write(this.path, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Writes content to file.
     *
     * @param data Data to write in {@code String} format.
     * @return The {@link DeltaFile} instance.
     */
    public DeltaFile write(String data) {
        return this.write(data.getBytes(StandardCharsets.UTF_8));
    }
}
