package ua.mazik.javarune.font.glyph;

import ua.mazik.delta.util.Pixmap;
import ua.mazik.delta.util.TextureAtlas;
import ua.mazik.javarune.font.TextColor;
import ua.mazik.javarune.render.RenderContext;
import ua.mazik.javarune.shader.Shaders;

public record BitmapGlyph(Pixmap pixmap, String regionName, int u, int v, int width, int height) implements Glyph {
    @Override
    public void draw(RenderContext renderer, TextureAtlas atlas, int x, int y, int scale, TextColor color) {
        TextureAtlas.Region region = atlas.findRegion(this.regionName);

        if (region == null) {
            Pixmap characterPixmap = new Pixmap(this.width, this.height);
            characterPixmap.drawPixmap(0, 0, this.u, this.v, this.width, this.height, this.pixmap);
            atlas.add(this.regionName, characterPixmap);
            characterPixmap.close();

            region = atlas.findRegion(this.regionName);
        }

        if (region != null) {
            renderer.drawTexture(region.page().getTexture(), Shaders.TEXTURE, x, y, region.x(), region.y(), region.x() + region.width(), region.y() + region.height(), region.width() * 2, region.height() * 2, color.top(), color.top(), color.bottom(), color.bottom());
        }
    }

    @Override
    public int width() {
        return this.width + 2;
    }
}
