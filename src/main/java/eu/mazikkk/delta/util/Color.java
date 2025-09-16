package eu.mazikkk.delta.util;

import org.jspecify.annotations.NonNull;

public class Color {
    public final int red;
    public final int green;
    public final int blue;
    public final int alpha;

    public final int rgba;
    public final int argb;

    public Color(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;

        this.rgba = (red << 24) | (green << 16) | (blue << 8) | alpha;
        this.argb = (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static @NonNull Color fromRgba(int rgba) {
        int r = (rgba >> 24) & 0xFF;
        int g = (rgba >> 16) & 0xFF;
        int b = (rgba >> 8) & 0xFF;
        int a = rgba & 0xFF;

        return new Color(r, g, b, a);
    }

    public static @NonNull Color fromRgb(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        return new Color(r, g, b, 0xFF);
    }
}
