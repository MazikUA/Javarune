package ua.mazik.javarune.font;

import ua.mazik.delta.codec.Codec;
import ua.mazik.delta.codec.ObjectCodec;
import ua.mazik.delta.util.StringIdentifiable;
import ua.mazik.javarune.font.provider.FontProvider;
import ua.mazik.javarune.font.provider.PixmapProvider;
import ua.mazik.javarune.font.provider.SpaceProvider;

public enum FontType implements StringIdentifiable {
    PIXMAP("pixmap", PixmapProvider.CODEC),
    SPACE("space", SpaceProvider.CODEC);

    public static final Codec<FontType> CODEC = StringIdentifiable.createCodec(FontType::values);

    private final String key;
    private final ObjectCodec<? extends FontProvider> codec;

    FontType(String key, ObjectCodec<? extends FontProvider> codec) {
        this.key = key;
        this.codec = codec;
    }

    public ObjectCodec<? extends FontProvider> getCodec() {
        return this.codec;
    }

    @Override
    public String asString() {
        return key;
    }
}
