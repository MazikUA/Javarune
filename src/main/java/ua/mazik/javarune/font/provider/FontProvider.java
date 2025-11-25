package ua.mazik.javarune.font.provider;

import ua.mazik.delta.codec.Codec;
import ua.mazik.javarune.font.FontType;
import ua.mazik.javarune.font.glyph.Glyph;

import java.util.Map;

public interface FontProvider {
    Codec<FontProvider> CODEC = FontType.CODEC.dispatch("type", FontProvider::getType, FontType::getCodec);

    Map<Character, Glyph> getGlyphs();

    FontType getType();
}
