package ua.mazik.delta.util;

import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collections;

/**
 * Utility class for simplified usage of different Java & DeltaEngine functions.
 */
public final class DeltaUtil {
    private DeltaUtil() {
    }

    @SuppressWarnings({"DuplicateExpressions", "RedundantSuppression"})
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
