package ua.mazik.javarune.text.font.glyph;

import ua.mazik.javarune.assets.AssetHelper;

public interface Glyph {
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
    Glyph BROKEN = new Glyph() {
        @Override
        public void render(GlyphRenderContext context) {
            AssetHelper.brokenTexture().draw(context.x(), context.y(), 8 * context.scale(), 16 * context.scale());
        }

        @Override
        public int width() {
            return 8;
        }
    };

    void render(GlyphRenderContext context);

    int width();
}
