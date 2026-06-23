package ua.mazik.delta.assets;

import java.nio.charset.StandardCharsets;

public interface Asset {
    String fileName();

    byte[] getBytes();

    default String getContent() {
        return new String(this.getBytes(), StandardCharsets.UTF_8);
    }
}
