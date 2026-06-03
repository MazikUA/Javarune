package ua.mazik.javarune.screen;

import org.lwjgl.sdl.*;
import ua.mazik.delta.renderer.Renderer;
import ua.mazik.delta.util.Pixel;
import ua.mazik.delta.util.SDLUtil;
import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.Util;
import ua.mazik.javarune.controls.Key;

import static org.lwjgl.sdl.SDLRender.*;

public class MainMenuScreen extends Screen {
    @Override
    public void update() {
        if (Key.CONFIRM.isPressed()) {
            Javarune.playSound("snd_select");
        }
    }

    @Override
    public void render() {
        Renderer.setDrawColor(Pixel.rgb(0xFF0000));
        Util.clearViewport();

        SDLUtil.withStack(SDL_FRect::malloc, rect -> {
            rect.set(300, 60, 219, 24);

            SDL_RenderTexture(Renderer.address, Javarune.texture("misc/logo").sdlTexture, null, rect);
        });

        /*drawTextWithShadow(" !\"#$%&'()*+,-./", 50, 120, 2, TextColor.RED);
        drawTextWithShadow("0123456789:;<=>?", 50, 160, 2, TextColor.A);
        drawTextWithShadow("@ABCDEFGHIJKLMNO", 50, 200, 2, TextColor.S);
        drawTextWithShadow("PQRSTUVWXYZ[\\]^_", 50, 240, 2, TextColor.V);
        drawTextWithShadow("`abcdefghijklmno", 50, 280, 2, TextColor.YELLOW);
        drawTextWithShadow("pqrstuvwxyz{|}` ", 50, 320, 2, TextColor.BLUE);*/
    }

    /* public void drawTextWithShadow(String text, int x, int y, int scale, TextColor color) {
        TextColor shadow = color == TextColor.WHITE ? TextColor.SHADOW : TextColor.full(color.transparency(77).bottom());

        this.drawText(text, x + 1, y + 1, scale * 0.5, shadow);
        this.drawText(text, x, y, scale * 0.5, color);
    }

    public void drawText(String text, int x, int y, int scale, Font font, TextColor color) {
        for (char character : text.toCharArray()) {
            Glyph glyph = font.glyphs.getOrDefault(character, Glyph.BROKEN);

            glyph.draw(font.atlas, x, y, scale, color);

            x += glyph.width() * scale;
        }
    }

    public void drawText(String text, int x, int y, int scale, String font, TextColor color) {
        this.drawText(text, x, y, scale, Javarune.font(font), color);
    }

    public void drawText(String text, int x, int y, int scale, TextColor color) {
        this.drawText(text, x, y, scale, "default", color);
    }*/
}
