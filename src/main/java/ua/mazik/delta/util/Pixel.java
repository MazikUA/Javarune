package ua.mazik.delta.util;

import org.jspecify.annotations.NonNull;

import java.nio.ByteBuffer;

public record Pixel(int red, int green, int blue, int alpha) {
    public static final Pixel RED = new Pixel(255, 0, 0, 255);
    public static final Pixel BLUE = new Pixel(0, 0, 255, 255);
    public static final Pixel YELLOW = new Pixel(255, 255, 0, 255);
    public static final Pixel LIME = new Pixel(0, 255, 0, 255);
    public static final Pixel WHITE = new Pixel(255, 255, 255, 255);
    public static final Pixel BLACK = new Pixel(0, 0, 0, 255);
    public static final Pixel PURPLE = new Pixel(128, 0, 128, 255);
    public static final Pixel MAROON = new Pixel(128, 0, 0, 255);
    public static final Pixel ORANGE = new Pixel(255, 159, 64, 255);
    public static final Pixel DARK_GRAY = new Pixel(80, 80, 80, 255);
    public static final Pixel NAVY = new Pixel(0, 0, 128, 255);

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
