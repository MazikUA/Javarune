package eu.mazikkk.javarune.font;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import eu.mazikkk.javarune.codec.Codec;
import eu.mazikkk.javarune.codec.RecordCodec;
import eu.mazikkk.javarune.draw.DrawContext;
import eu.mazikkk.javarune.font.glyph.Glyph;
import eu.mazikkk.javarune.font.provider.FontProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Font implements Disposable {
    public static final int LINE_HEIGHT = 16;
    public static final Codec<Font> CODEC = RecordCodec.create(
        FontProvider.CODEC.list().propertyOf("providers").getter(font -> font.providers),
        Font::new
    ).toCodec();

    public final List<FontProvider> providers;
    public final Map<Character, Glyph> glyphs = new HashMap<>();
    public final TextureAtlas atlas = new TextureAtlas();
    public final PixmapPacker packer = new PixmapPacker(2048, 2048, Pixmap.Format.RGBA8888, 0, false);

    public Font(List<FontProvider> providers) {
        this.providers = providers;

        for (FontProvider provider : providers) {
            glyphs.putAll(provider.getGlyphs());
        }
    }

    public void draw(DrawContext context, String text, float x, float y) {
        for (char character : text.toCharArray()) {
            Glyph glyph = this.glyphs.getOrDefault(character, Glyph.BROKEN);

            glyph.draw(context, x, y, this);

            x += glyph.width();
        }
    }

    public void updateAtlas() {
        this.packer.updateTextureAtlas(this.atlas, Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest, false);
    }

    @Override
    public void dispose() {
        this.atlas.dispose();
        this.packer.dispose();
        this.providers.clear();
    }
}
