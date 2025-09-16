package eu.mazikkk.javarune.font.glyph;

import eu.mazikkk.delta.Renderer;
import eu.mazikkk.delta.util.Pixmap;
import eu.mazikkk.delta.util.TextureAtlas;
import eu.mazikkk.javarune.Javarune;
import eu.mazikkk.javarune.font.Font;

public record BitmapGlyph(Pixmap pixmap, String regionName, int u, int v, int width, int height) implements Glyph {
    @Override
    public void draw(Renderer renderer, int x, int y, Font font) {
        TextureAtlas.Region region = font.atlas.findRegion(this.regionName);

        if (region == null) {
            Pixmap characterPixmap = new Pixmap(this.width, this.height);
            characterPixmap.drawPixmap(0, 0, this.u, this.v, this.width, this.height, this.pixmap);
            font.atlas.add(this.regionName, characterPixmap);
            characterPixmap.close();

            region = font.atlas.findRegion(this.regionName);
        }

        renderer.drawRegion(region, Javarune.getInstance().shaderLoader.get("texture").orElseThrow(), x, y, region.width(), region.height());
    }

    @Override
    public int width() {
        return this.width + 2;
    }
}
