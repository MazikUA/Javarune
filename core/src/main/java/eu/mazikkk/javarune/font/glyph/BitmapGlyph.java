package eu.mazikkk.javarune.font.glyph;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import eu.mazikkk.javarune.draw.DrawContext;
import eu.mazikkk.javarune.font.Font;

public record BitmapGlyph(Pixmap pixmap, String regionName, int u, int v, int width, int height, int ascent) implements Glyph {
    @Override
    public void draw(DrawContext context, float x, float y, Font font) {
        TextureAtlas.AtlasRegion region = font.atlas.findRegion(this.regionName);

        if (region == null) {
            Pixmap characterPixmap = new Pixmap(this.width, this.height, this.pixmap.getFormat());
            characterPixmap.drawPixmap(this.pixmap, 0, 0, this.u, this.v, this.width, this.height);
            font.packer.pack(this.regionName, characterPixmap);
            characterPixmap.dispose();

            font.updateAtlas();
            region = font.atlas.findRegion(this.regionName);
        }

        context.drawRegion(region, x, y - this.height + this.ascent);
    }

    @Override
    public int width() {
        return this.width + 2;
    }
}
