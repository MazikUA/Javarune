package ua.mazik.javarune.text.font;

import ua.mazik.javarune.text.font.glyph.Glyph;

import java.util.Map;
import java.util.Optional;

public final class BrokenFont extends Font {
    public static final BrokenFont INSTANCE = new BrokenFont();

    private BrokenFont() {
        super(null, null);
    }

    @Override
    public Optional<Glyph> getGlyph(char character, Map<Condition, Object> overrides) {
        return Optional.of(Glyph.BROKEN);
    }
}
