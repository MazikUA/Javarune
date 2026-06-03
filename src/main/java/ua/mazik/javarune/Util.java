package ua.mazik.javarune;

import org.jspecify.annotations.Nullable;
import org.lwjgl.sdl.*;
import ua.mazik.delta.renderer.Renderer;
import ua.mazik.delta.util.SDLUtil;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collections;

import static org.lwjgl.sdl.SDLRender.*;

public class Util {
    @SuppressWarnings({"resource", "DuplicateExpressions", "RedundantSuppression"})
    public static @Nullable Path getPath(URI uri) throws IOException {
        try {
            return Path.of(uri);
        } catch (FileSystemNotFoundException ignored) {
            FileSystems.newFileSystem(uri, Collections.emptyMap());
            return Path.of(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void clearViewport() {
        SDLUtil.withStack(SDL_FRect::malloc, (rect) -> {
            rect.set(0, 0, 640, 480);

            SDL_RenderFillRect(Renderer.address, rect);
        });
    }
}
