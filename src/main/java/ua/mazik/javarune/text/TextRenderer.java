package ua.mazik.javarune.text;

import ua.mazik.delta.sdl.texture.SDLTextureAtlas;
import ua.mazik.delta.util.Pixel;
import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.text.font.Font;
import ua.mazik.javarune.text.font.glyph.Glyph;

import java.util.Optional;
import java.util.function.Supplier;

public final class TextRenderer {
    public static final int COLORED_SHADOW_ALPHA = 77;
    public static final int FONT_ATLAS_SIZE = 2048;

    private TextRenderer() {
    }

    public static void renderText(Text text, int x, int y, int scale, boolean darkWorld) {
        Optional<Font> font = Javarune.fontLoader().get("main");

        if (font.isEmpty()) return;

        Supplier<SDLTextureAtlas> atlas = () -> Javarune.atlasManager().getOrCreate("main", FONT_ATLAS_SIZE);

        int textX = x;

        for (Text child : text.getAllChildren()) {
            String content = child.getContent();

            for (char character : content.toCharArray()) {
                Glyph glyph = font.get().getGlyph(character).orElse(Glyph.BROKEN);

                if (darkWorld) {
                    Pixel topColor = child.color == Pixel.WHITE ? Pixel.DARK_GRAY : child.color.withAlpha(COLORED_SHADOW_ALPHA);
                    Pixel bottomColor = child.color == Pixel.WHITE ? Pixel.NAVY : topColor;

                    glyph.render(atlas, textX + 1, y + 1, scale, topColor, bottomColor);
                }

                glyph.render(atlas, textX, y, scale, darkWorld ? Pixel.WHITE : child.color, child.color);

                textX += (glyph.width() + 1) * scale;
            }
        }
    }

    public static void renderText(Text text, int x, int y) {
        renderText(text, x, y, 2, true);
    }
}
