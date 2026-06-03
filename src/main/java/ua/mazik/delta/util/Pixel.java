package ua.mazik.delta.util;

import org.jspecify.annotations.NonNull;

/**
 * The {@link Pixel} class is used in Delta Engine for functions which
 * interacts with colors. This class uses RGBA instead of ARGB, which uses in {@link java.awt.Color}.
 * <br />
 * <br />
 * All params should be between {@code 0} and {@code 255}.
 *
 * @param red
 * @param green
 * @param blue
 * @param alpha
 */
@SuppressWarnings("MagicNumber")
public record Pixel(int red, int green, int blue, int alpha) {
    public static final Pixel BLACK = new Pixel(0, 0, 0, 255);
    public static final Pixel RED = new Pixel(255, 0, 0, 255);
    public static final Pixel BLUE = new Pixel(0, 0, 255, 255);
    public static final Pixel YELLOW = new Pixel(255, 255, 0, 255);
    public static final Pixel WHITE = new Pixel(255, 255, 255, 255);

    /**
     * Converts 32-bit RGBA to {@link Pixel}.
     *
     * @param rgba RGBA packed integer.
     * @return Converted 32-bit {@code rgba} to {@link Pixel}.
     */
    public static @NonNull Pixel rgba(int rgba) {
        int r = (rgba >> 24) & 0xFF;
        int g = (rgba >> 16) & 0xFF;
        int b = (rgba >> 8) & 0xFF;
        int a = rgba & 0xFF;

        return new Pixel(r, g, b, a);
    }

    /**
     * Converts 24-bit RGB to {@link Pixel}.
     *
     * @param rgb RGB packed integer.
     * @return Converted 24-bit {@code rgba} to {@link Pixel}, where {@code alpha} equals {@code 0xFF}.
     */
    public static @NonNull Pixel rgb(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        return new Pixel(r, g, b, 0xFF);
    }

    /**
     * Converts {@link Pixel} to 32-bit RGBA. Mostly uses with graphic interactions.
     *
     * @return RGBA packed integer;
     */
    public int rgba() {
        return (this.red << 24) | (this.green << 16) | (this.blue << 8) | this.alpha;
    }

    /**
     * Converts {@link Pixel} to 32-bit ARGB. Mostly uses with Java interactions, where ARGB is used instead of RGBA.
     *
     * @return ARGB packed integer;
     */
    public int argb() {
        return (this.alpha << 24) | (this.red << 16) | (this.green << 8) | this.blue;
    }
}
