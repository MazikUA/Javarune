package ua.mazik.javarune.font;

import ua.mazik.delta.util.Pixel;

public record TextColor(Pixel top, Pixel bottom) {
    public static final TextColor WHITE = full(Pixel.WHITE);
    public static final TextColor RED = bottom(Pixel.RED);
    public static final TextColor BLUE = bottom(Pixel.BLUE);
    public static final TextColor YELLOW = bottom(Pixel.YELLOW);
    public static final TextColor LIME = bottom(Pixel.LIME);
    public static final TextColor BLACK = bottom(Pixel.BLACK);
    public static final TextColor PURPLE = bottom(Pixel.PURPLE);
    public static final TextColor MAROON = bottom(Pixel.MAROON);
    public static final TextColor ORANGE = bottom(Pixel.ORANGE);
    public static final TextColor A = bottom(Pixel.rgb(0x00AEFF));
    public static final TextColor S = bottom(Pixel.rgb(0xFF80FF));
    public static final TextColor V = bottom(Pixel.rgb(0x80FF80));
    public static final TextColor I = bottom(Pixel.rgb(0x81C0FF));

    public static final TextColor SHADOW = new TextColor(Pixel.DARK_GRAY, Pixel.NAVY);

    public static TextColor full(Pixel color) {
        return new TextColor(color, color);
    }

    public static TextColor bottom(Pixel color) {
        return new TextColor(Pixel.WHITE, color);
    }

    public TextColor transparency(int transparency) {
        return new TextColor(new Pixel(this.top.red(), this.top.green(), this.top.blue(), transparency), new Pixel(this.bottom.red(), this.bottom.green(), this.bottom.blue(), transparency));
    }
}
