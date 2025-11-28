package ua.mazik.javarune.font.glyph;

import ua.mazik.javarune.font.Font;
import ua.mazik.javarune.render.RenderContext;
import ua.mazik.javarune.render.Shaders;

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
    public void draw(RenderContext renderer, int x, int y, Font font) {
        renderer.drawTexture("misc/assgore", Shaders.TEXTURE, x, y, 0, 0, 40, 40, 8, 16);
    }

    @Override
    public int width() {
        return 10;
    }
}
