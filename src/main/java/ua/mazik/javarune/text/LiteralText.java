package ua.mazik.javarune.text;

public class LiteralText extends Text {
    public final String content;

    public LiteralText(String content) {
        super();

        this.content = content;
    }

    @Override
    public String getContent() {
        return this.content;
    }
}
