package ua.mazik.javarune.text;

import ua.mazik.delta.util.Pixel;

import java.util.ArrayList;
import java.util.List;

public abstract class Text {
    public final List<Text> children;
    public Pixel color;

    public Text() {
        this.children = new ArrayList<>();
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
