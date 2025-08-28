package eu.mazikkk.javarune.font.provider;

import com.badlogic.gdx.graphics.Pixmap;
import eu.mazikkk.javarune.asset.PixmapAsset;
import eu.mazikkk.javarune.codec.Codec;
import eu.mazikkk.javarune.codec.Codecs;
import eu.mazikkk.javarune.codec.ObjectCodec;
import eu.mazikkk.javarune.codec.RecordCodec;
import eu.mazikkk.javarune.font.FontType;
import eu.mazikkk.javarune.font.glyph.BitmapGlyph;
import eu.mazikkk.javarune.font.glyph.Glyph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record PixmapProvider(PixmapAsset file, int height, char[][] glyphs) implements FontProvider {
    public static final Codec<char[][]> CHAR_GRID = Codecs.STRING.list().map(
        strings -> {
            int rows = strings.size();
            char[][] chars = new char[rows][];

            for (int i = 0; i < rows; i++) {
                chars[i] = strings.get(i).toCharArray();
            }

            return chars;
        },
        chars -> {
            List<String> strings = new ArrayList<>();

            for (char[] row : chars) {
                strings.add(new String(row));
            }

            return strings;
        }
    );
    public static final ObjectCodec<PixmapProvider> CODEC = RecordCodec.create(
        Codecs.PIXMAP.propertyOf("file").getter(PixmapProvider::file),
        Codecs.INT.optional(16).propertyOf("height").getter(PixmapProvider::height),
        CHAR_GRID.propertyOf("glyphs").getter(PixmapProvider::glyphs),
        PixmapProvider::new
    );

    @Override
    public Map<Character, Glyph> getGlyphs() {
        Map<Character, Glyph> glyphs = new HashMap<>();

        int rows = this.glyphs.length;
        int cols = this.glyphs[0].length;

        Pixmap pixmap = this.file.pixmap();

        int cellWidth = pixmap.getWidth() / cols;
        int cellHeight = pixmap.getHeight() / rows;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char character = this.glyphs[row][col];

                if (character == ' ') continue;

                int u = cellWidth * col;
                int v = cellHeight * row;

                int width = getWidth(pixmap, u, v, cellWidth, cellHeight);

                if (width == 0) continue;

                Glyph glyph = new BitmapGlyph(pixmap, String.valueOf(character), u, v, width, cellHeight);

                glyphs.put(character, glyph);
            }
        }

        return glyphs;
    }

    private int getWidth(Pixmap pixmap, int cellX, int cellY, int cellWidth, int cellHeight) {
        for (int x = cellWidth - 1; x >= 0; x--) {
            for (int y = 0; y < cellHeight; y++) {
                int pixel = pixmap.getPixel(cellX + x, cellY + y);
                int alpha = pixel & 0xFF;

                if (alpha != 0) {
                    return x + 1;
                }
            }
        }

        return 0;
    }

    @Override
    public FontType getType() {
        return FontType.PIXMAP;
    }
}
