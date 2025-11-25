package ua.mazik.javarune.font.glyph;

import ua.mazik.delta.renderer.Renderer;
import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.font.Font;

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
    public void draw(Renderer renderer, int x, int y, Font font) {
        renderer.drawTexture(Javarune.texture("misc/assgore"), Javarune.getInstance().shaderLoader.get("texture").orElseThrow(), x, y, 0, 0, 8, 16, 40, 40, 40, 40);
    }

    @Override
    public int width() {
        return 10;
    }
}
