package eu.mazikkk.javarune.font;

import eu.mazikkk.javarune.codec.Codec;
import eu.mazikkk.javarune.codec.ObjectCodec;
import eu.mazikkk.javarune.font.provider.FontProvider;
import eu.mazikkk.javarune.font.provider.PixmapProvider;
import eu.mazikkk.javarune.font.provider.SpaceProvider;
import eu.mazikkk.javarune.util.StringIdentifiable;

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
