package ua.mazik.javarune.render;

import ua.mazik.delta.renderer.Shader;
import ua.mazik.delta.renderer.Texture;
import ua.mazik.delta.renderer.draw.DrawElement;
import ua.mazik.delta.renderer.draw.TextureDrawElement;
import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.font.Font;
import ua.mazik.javarune.font.glyph.Glyph;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class RenderContext {
    public final List<DrawElement> elements = new ArrayList<>();

    public void drawTexture(String texture, Supplier<Optional<Shader>> shaderSupplier, int x, int y, int u0, int v0, int u1, int v1, int width, int height) {
        this.drawTexture(Javarune.texture(texture), shaderSupplier, x, y, u0, v0, u1, v1, width, height);
    }

    public void drawTexture(Texture texture, Supplier<Optional<Shader>> shaderSupplier, int x, int y, int u0, int v0, int u1, int v1, int width, int height) {
        this.elements.add(
                new TextureDrawElement(texture, shaderSupplier.get().orElseThrow(), x, y, u0, v0, u1, v1, width, height)
        );
    }

    public void drawText(String text, int x, int y, Font font) {
        for (char character : text.toCharArray()) {
            Glyph glyph = font.glyphs.getOrDefault(character, Glyph.BROKEN);

            glyph.draw(this, font.atlas, x, y);

            x += glyph.width();
        }
    }

    public void drawText(String text, int x, int y, String font) {
        this.drawText(text, x, y, Javarune.font(font));
    }

    public void drawText(String text, int x, int y) {
        this.drawText(text, x, y, "default");
    }
}
