package ua.mazik.javarune.text;

import ua.mazik.delta.sdl.texture.SDLTextureAtlas;
import ua.mazik.delta.util.Pixel;
import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.text.font.BrokenFont;
import ua.mazik.javarune.text.font.Font;
import ua.mazik.javarune.text.font.glyph.Glyph;
import ua.mazik.javarune.text.font.glyph.GlyphRenderContext;

import java.util.function.Supplier;

public final class TextRenderer {
    public static final int COLORED_SHADOW_ALPHA = 77;
    public static final int FONT_ATLAS_SIZE = 2048;

    private TextRenderer() {
    }

    public static void render(Text text, int x, int y, int scale, TextLocation location) {
        int textX = x;

        for (Text child : text.getAllChildren()) {
            Supplier<SDLTextureAtlas> atlas = () -> Javarune.atlasManager().getOrCreate(child.font, FONT_ATLAS_SIZE);
            Font font = Javarune.fontLoader().get(child.font).orElse(BrokenFont.INSTANCE);

            String content = child.content;

            for (char character : content.toCharArray()) {
                Glyph glyph = font.getGlyph(character, child.overrides).orElse(Glyph.BROKEN);

                if (location == TextLocation.DIALOG_DARK) {
                    Pixel topColor = child.color == Pixel.WHITE ? Pixel.DARK_GRAY : child.color.withAlpha(COLORED_SHADOW_ALPHA);
                    Pixel bottomColor = child.color == Pixel.WHITE ? Pixel.NAVY : topColor;

                    glyph.render(new GlyphRenderContext(atlas, textX + 1, y + 1, scale, topColor, bottomColor));
                }

                glyph.render(new GlyphRenderContext(atlas, textX, y, scale, location == TextLocation.DIALOG_DARK ? Pixel.WHITE : child.color, child.color));

                int glyphWidth = location == TextLocation.DIALOG || location == TextLocation.DIALOG_DARK
                    ? Javarune.SETTINGS.dialogGlyphWidth.get().converter.apply(glyph.width())
                    : glyph.width();

                textX += (glyphWidth + 1) * scale;
            }
        }
    }

    public static void render(Text text, int x, int y) {
        render(text, x, y, 2, TextLocation.DEFAULT);
    }

    public enum TextLocation {
        DEFAULT,
        DIALOG,
        DIALOG_DARK
    }
}
