package ua.mazik.javarune.text;

import ua.mazik.javarune.Javarune;

public class TranslatableText extends Text {
    public final String key;

    public TranslatableText(String key) {
        super();

        this.key = key;
    }

    @Override
    public String getContent() {
        return Javarune.languageManager().get(this.key).orElse(this.key);
    }
}
