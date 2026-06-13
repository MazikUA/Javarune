package ua.mazik.javarune.text;

import ua.mazik.delta.util.Pixel;
import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.text.font.Font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Text {
    public final String content;

    public final List<Text> children;
    public final Map<Font.Condition, Object> overrides;

    public Pixel color;
    public String font;

    public Text(String content) {
        this.content = content;

        this.children = new ArrayList<>();
        this.overrides = new HashMap<>();

        this.color = Pixel.WHITE;
        this.font = "main";
    }

    public static Text translated(String key) {
        return Javarune.languageManager().get(key).orElse(new Text(key));
    }

    public Text add(Text text) {
        this.children.add(text);
        return this;
    }

    public Text overrides(Font.Condition condition, Object value) {
        this.overrides.put(condition, value);
        return this;
    }

    public Text color(Pixel color) {
        this.color = color;
        return this;
    }

    public Text font(String font) {
        this.font = font;
        return this;
    }

    public List<Text> getAllChildren() {
        List<Text> allChildren = new ArrayList<>();

        allChildren.add(this);

        for (Text text : this.children) {
            allChildren.addAll(text.getAllChildren());
        }

        return allChildren;
    }

    public Text copy() {
        Text text = new Text(this.content);

        for (Text child : this.children) {
            text.children.add(child.copy());
        }

        text.overrides.putAll(this.overrides);

        text.color = this.color.copy();
        text.font = this.font;

        return text;
    }
}
