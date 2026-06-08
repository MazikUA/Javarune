package ua.mazik.javarune.text;

import ua.mazik.delta.util.Pixel;
import ua.mazik.javarune.text.font.Font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Text {
    public final List<Text> children;
    public final Map<Font.Condition, Object> overrides;
    public Pixel color;

    public Text() {
        this.children = new ArrayList<>();
        this.overrides = new HashMap<>();
        this.color = Pixel.WHITE;
    }

    public Text add(Text text) {
        this.children.add(text);
        return this;
    }

    public Text color(Pixel color) {
        this.color = color;
        return this;
    }

    public Text overrides(Font.Condition condition, Object value) {
        this.overrides.put(condition, value);
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

    public abstract String getContent();
}
