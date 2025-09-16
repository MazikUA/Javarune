package eu.mazikkk.javarune.font.provider;

import eu.mazikkk.delta.codec.Codec;
import eu.mazikkk.javarune.font.FontType;
import eu.mazikkk.javarune.font.glyph.Glyph;

import java.util.Map;

public interface FontProvider {
    Codec<FontProvider> CODEC = FontType.CODEC.dispatch("type", FontProvider::getType, FontType::getCodec);

    Map<Character, Glyph> getGlyphs();

    FontType getType();
}
