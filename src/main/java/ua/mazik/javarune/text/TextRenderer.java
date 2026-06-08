package ua.mazik.javarune.text;

import ua.mazik.delta.sdl.texture.SDLTextureAtlas;
import ua.mazik.delta.util.Pixel;
import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.text.font.Font;
import ua.mazik.javarune.text.font.glyph.Glyph;
import ua.mazik.javarune.text.font.glyph.GlyphRenderContext;

import java.util.Optional;
import java.util.function.Supplier;

public final class TextRenderer {
    public static final int COLORED_SHADOW_ALPHA = 77;
    public static final int FONT_ATLAS_SIZE = 2048;

    private TextRenderer() {
    }

    public static void renderText(Text text, int x, int y, int scale, TextLocation location) {
        Optional<Font> font = Javarune.fontLoader().get("main");

        if (font.isEmpty()) return;

        Supplier<SDLTextureAtlas> atlas = () -> Javarune.atlasManager().getOrCreate("main", FONT_ATLAS_SIZE);

        int textX = x;

        for (Text child : text.getAllChildren()) {
            String content = child.getContent();

            for (char character : content.toCharArray()) {
                Glyph glyph = font.get().getGlyph(character, child.overrides).orElse(Glyph.BROKEN);

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

    public static void renderText(Text text, int x, int y) {
        renderText(text, x, y, 2, TextLocation.DEFAULT);
    }

    public enum TextLocation {
        DEFAULT,
        DIALOG,
        DIALOG_DARK
    }
}
