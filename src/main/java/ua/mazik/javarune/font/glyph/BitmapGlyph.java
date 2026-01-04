package ua.mazik.javarune.font.glyph;

import ua.mazik.delta.util.Pixmap;
import ua.mazik.delta.util.TextureAtlas;
import ua.mazik.javarune.render.RenderContext;
import ua.mazik.javarune.render.Shaders;

public record BitmapGlyph(Pixmap pixmap, String regionName, int u, int v, int width, int height) implements Glyph {
    @Override
    public void draw(RenderContext renderer, TextureAtlas atlas, int x, int y) {
        TextureAtlas.Region region = atlas.findRegion(this.regionName);

        if (region == null) {
            Pixmap characterPixmap = new Pixmap(this.width, this.height);
            characterPixmap.drawPixmap(0, 0, this.u, this.v, this.width, this.height, this.pixmap);
            atlas.add(this.regionName, characterPixmap);
            characterPixmap.close();

            region = atlas.findRegion(this.regionName);
        }

        if (region != null) {
            region.draw(renderer, Shaders.TEXTURE, x, y);
        }
    }

    @Override
    public int width() {
        return this.width + 2;
    }
}
