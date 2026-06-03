package ua.mazik.javarune.render;

import ua.mazik.delta.renderer.Shader;
import ua.mazik.delta.renderer.Texture;
import ua.mazik.delta.renderer.draw.DrawElement;
import ua.mazik.delta.util.Pixel;
import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.font.Font;
import ua.mazik.javarune.font.TextColor;
import ua.mazik.javarune.font.glyph.Glyph;
import ua.mazik.javarune.shader.TextureDrawElement;

import java.util.ArrayList;
import java.util.List;

public class RenderContext {
    public final List<DrawElement<?>> elements = new ArrayList<>();

    public void drawTexture(String texture, Shader shaderSupplier, int x, int y, int u0, int v0, int u1, int v1, int width, int height) {
        this.drawTexture(Javarune.texture(texture), shaderSupplier, x, y, u0, v0, u1, v1, width, height);
    }

    public void drawTexture(Texture texture, Shader shaderSupplier, int x, int y, int u0, int v0, int u1, int v1, int width, int height) {
        this.drawTexture(texture, shaderSupplier, x, y, u0, v0, u1, v1, width, height, Pixel.WHITE, Pixel.WHITE, Pixel.WHITE, Pixel.WHITE);
    }

    public void drawTexture(Texture texture, Shader shaderSupplier, int x, int y, int u0, int v0, int u1, int v1, int width, int height, Pixel color) {
        this.drawTexture(texture, shaderSupplier, x, y, u0, v0, u1, v1, width, height, color, color, color, color);
    }

    public void drawTexture(Texture texture, Shader shaderSupplier, int x, int y, int u0, int v0, int u1, int v1, int width, int height, Pixel topLeftColor, Pixel topRightColor, Pixel bottomRightColor, Pixel bottomLeftColor) {
        this.elements.add(
                new TextureDrawElement(texture, shaderSupplier, x, y, u0, v0, u1, v1, width, height, topLeftColor, topRightColor, bottomRightColor, bottomLeftColor)
        );
    }

    public void drawText(String text, int x, int y, int scale, Font font, TextColor color) {
        for (char character : text.toCharArray()) {
            Glyph glyph = font.glyphs.getOrDefault(character, Glyph.BROKEN);

            glyph.draw(this, font.atlas, x, y, scale, color);

            x += glyph.width() * scale;
        }
    }

    public void drawText(String text, int x, int y, int scale, String font, TextColor color) {
        this.drawText(text, x, y, scale, Javarune.font(font), color);
    }

    public void drawText(String text, int x, int y, int scale, TextColor color) {
        this.drawText(text, x, y, scale, "default", color);
    }

    public void drawTextWithShadow(String text, int x, int y, int scale, TextColor color) {
        TextColor shadow = color == TextColor.WHITE ? TextColor.SHADOW : TextColor.full(color.transparency(77).bottom());

        this.drawText(text, x + 1, y + 1, scale, shadow);
        this.drawText(text, x, y, scale, color);
    }
}
