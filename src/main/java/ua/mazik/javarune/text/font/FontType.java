package ua.mazik.javarune.text.font;

import ua.mazik.delta.codec.ObjectCodec;
import ua.mazik.delta.util.StringIdentifiable;

public enum FontType implements StringIdentifiable {
    TEXTURE(TextureFont.CODEC),
    SPACE(SpaceFont.CODEC),
    INCLUDE(IncludeFont.CODEC),
    COMPOSE(ComposeFont.CODEC);

    private final ObjectCodec<? extends Font> codec;

    FontType(ObjectCodec<? extends Font> codec) {
        this.codec = codec;
    }

    public ObjectCodec<? extends Font> codec() {
        return this.codec;
    }

    @Override
    public String asString() {
        return this.name().toLowerCase();
    }
}
