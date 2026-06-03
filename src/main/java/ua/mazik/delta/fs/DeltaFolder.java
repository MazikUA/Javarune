package ua.mazik.delta.fs;

import org.lwjgl.sdl.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.lwjgl.sdl.SDLFileSystem.*;

/**
 * The {@link DeltaFolder} class is designed for simple usage of folders,
 * but for more complex actions prefer to use {@link java.nio} package.
 *
 * @param path Folder path.
 */
public record DeltaFolder(Path path) {
    /**
     * Creates new {@link DeltaFolder} instance, located in:
     * <ul>
     *     <li>Windows — {@code C:/Users/username/AppData/Roaming/Organization name/Application name/}</li>
     *     <li>MacOS — {@code /Users/username/Library/Application Support/Application name/}</li>
     *     <li>Linux — {@code /home/username/.local/share/Application name/}</li>
     * </ul>
     *
     * @param org Organization name.
     * @param app Application name.
     * @return {@link Optional} with {@link DeltaFolder} instance. If {@link SDLFileSystem#SDL_GetPrefPath} returns {@code null}, empty {@link Optional} is returned.
     */
    public static Optional<DeltaFolder> appData(String org, String app) {
        String path = SDL_GetPrefPath(org, app);

        if (path == null) {
            return Optional.empty();
        } else {
            return Optional.of(new DeltaFolder(Paths.get(path)));
        }
    }

    /**
     * Creates new {@link DeltaFolder} instance, inside this {@link DeltaFolder}.
     *
     * @param name Folder name.
     * @return {@link DeltaFolder} instance.
     */
    public DeltaFolder folder(String name) {
        return new DeltaFolder(this.path.resolve(name));
    }

    /**
     * Creates new {@link DeltaFile} instance, inside this {@link DeltaFolder}.
     *
     * @param name File name.
     * @return {@link DeltaFile} instance.
     */
    public DeltaFile file(String name) {
        return new DeltaFile(this.path.resolve(name), this);
    }

    /**
     * Creates non-existing folders in path.
     *
     * @return This {@link DeltaFolder} instance.
     */
    public DeltaFolder mkdirs() {
        try {
            Files.createDirectories(this.path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
}
