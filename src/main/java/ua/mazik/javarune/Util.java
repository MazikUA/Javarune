package ua.mazik.javarune;

import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collections;

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
}
