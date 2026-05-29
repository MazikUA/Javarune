package ua.mazik.javarune.screen;

import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.controls.Key;
import ua.mazik.javarune.font.TextColor;
import ua.mazik.javarune.render.RenderContext;
import ua.mazik.javarune.shader.Shaders;

public class MainMenuScreen extends Screen {
    @Override
    public void update() {
        if (Key.CONFIRM.isPressed()) {
            Javarune.playSound("snd_select");
        }
    }

    @Override
    public void render(RenderContext ctx) {
        ctx.drawTexture(
                "misc/logo",
                Shaders.TEXTURE,
                60, 60,
                0, 0,
                219, 24,
                219, 24
        );

        ctx.drawTextWithShadow(" !\"#$%&'()*+,-./", 50, 120, 2, TextColor.RED);
        ctx.drawTextWithShadow("0123456789:;<=>?", 50, 160, 2, TextColor.A);
        ctx.drawTextWithShadow("@ABCDEFGHIJKLMNO", 50, 200, 2, TextColor.S);
        ctx.drawTextWithShadow("PQRSTUVWXYZ[\\]^_", 50, 240, 2, TextColor.V);
        ctx.drawTextWithShadow("`abcdefghijklmno", 50, 280, 2, TextColor.YELLOW);
        ctx.drawTextWithShadow("pqrstuvwxyz{|}` ", 50, 320, 2, TextColor.BLUE);
    }
}
