package ua.mazik.delta.util;

import org.jspecify.annotations.NonNull;

import java.nio.ByteBuffer;

public record Pixel(int red, int green, int blue, int alpha) {
    public static final Pixel WHITE = new Pixel(255, 255, 255, 255);

    public static @NonNull Pixel rgba(int rgba) {
        int r = (rgba >> 24) & 0xFF;
        int g = (rgba >> 16) & 0xFF;
        int b = (rgba >> 8) & 0xFF;
        int a = rgba & 0xFF;

        return new Pixel(r, g, b, a);
    }

    public static @NonNull Pixel rgb(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        return new Pixel(r, g, b, 0xFF);
    }

    public int rgba() {
        return (this.red << 24) | (this.green << 16) | (this.blue << 8) | this.alpha;
    }

    public int argb() {
        return (this.alpha << 24) | (this.red << 16) | (this.green << 8) | this.blue;
    }

    public void put(ByteBuffer buffer) {
        buffer.putFloat(this.red / 255f).putFloat(this.green / 255f).putFloat(this.blue / 255f).putFloat(this.alpha / 255f);
    }
}
