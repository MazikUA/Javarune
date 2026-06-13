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
    // GameMaker
    public static final Pixel AQUA = Pixel.rgb(0x00FF00FF);
    public static final Pixel BLACK = Pixel.rgb(0x000000);
    public static final Pixel BLUE = Pixel.rgb(0x0000FF);
    public static final Pixel DARK_GRAY = Pixel.rgb(0x404040);
    public static final Pixel FUCHSIA = Pixel.rgb(0xFF00FF);
    public static final Pixel GRAY = Pixel.rgb(0x808080);
    public static final Pixel GREEN = Pixel.rgb(0x008000);
    public static final Pixel LIME = Pixel.rgb(0x00FF00);
    public static final Pixel LIGHT_GRAY = Pixel.rgb(0xC0C0C0);
    public static final Pixel MAROON = Pixel.rgb(0x800000);
    public static final Pixel NAVY = Pixel.rgb(0x000080);
    public static final Pixel OLIVE = Pixel.rgb(0x008080);
    public static final Pixel ORANGE = Pixel.rgb(0xFF8040);
    public static final Pixel PURPLE = Pixel.rgb(0x800080);
    public static final Pixel RED = Pixel.rgb(0xFF0000);
    public static final Pixel SILVER = Pixel.rgb(0xC0C0C0);
    public static final Pixel TEAL = Pixel.rgb(0x008080);
    public static final Pixel WHITE = Pixel.rgb(0xFFFFFF);
    public static final Pixel YELLOW = Pixel.rgb(0xFFFF00);

    // Deltarune
    public static final Pixel AZURE = Pixel.rgb(0x00AEFF);
    public static final Pixel MAGENTA = Pixel.rgb(0xFF80FF); // toby... why «s» is magenta
    public static final Pixel VIRIDIAN = Pixel.rgb(0x80FF80);
    public static final Pixel ICE = Pixel.rgb(0x81C0FF);

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

    public Pixel withAlpha(int alpha) {
        return new Pixel(this.red, this.green, this.blue, alpha);
    }

    public Pixel copy() {
        return new Pixel(this.red, this.green, this.blue, this.alpha);
    }
}
