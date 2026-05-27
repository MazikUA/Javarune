package ua.mazik.javarune.font.glyph;

import ua.mazik.delta.util.TextureAtlas;
import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.font.TextColor;
import ua.mazik.javarune.render.RenderContext;
import ua.mazik.javarune.shader.Shaders;

/**
 * DRIVING IN MY CAR
 * RIGHT AFTER A BEER
 * HEY, THAT BUMP IS SHAPED LIKE A DEER
 * DUI?
 * HOW ABOUT YOU DIE
 * I'LL GO HUNDRED MILES!!
 * AN HOUR
 * LITTLE DO YOU KNOW,
 * I FILLED UP ON GAS
 * IMA GET YOUR FOUNTAIN MAKING ASS
 * PULVERIZE THIS F*CK!
 * WITH MY BERGENTRÜCK
 * IT SEEMS YOU'RE OUTTA LUCK
 * TRUCK!!
 */
public class AssgoreGlyph implements Glyph {
    @Override
    public void draw(RenderContext renderer, TextureAtlas atlas, int x, int y, int scale, TextColor color) {
        renderer.drawTexture(Javarune.texture("misc/assgore"), Shaders.TEXTURE, x, y, 0, 0, 40, 40, 8 * scale, 16 * scale, color.top(), color.top(), color.bottom(), color.bottom());
    }

    @Override
    public int width() {
        return 10;
    }
}
