package eu.mazikkk.javarune.font.glyph;

import eu.mazikkk.javarune.draw.DrawContext;
import eu.mazikkk.javarune.font.Font;

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
    public void draw(DrawContext context, float x, float y, Font font) {
        context.drawTexture("misc/assgore", x, y, 8, 16);
    }

    @Override
    public int width() {
        return 10;
    }
}
