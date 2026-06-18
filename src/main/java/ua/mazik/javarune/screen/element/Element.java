package ua.mazik.javarune.screen.element;

import ua.mazik.javarune.screen.Renderable;
import ua.mazik.javarune.screen.Screen;

public abstract class Element implements Renderable {
    public final Screen screen;
    public int x;
    public int y;

    public Element(int x, int y, Screen screen) {
        this.x = x;
        this.y = y;

        this.screen = screen;
    }
}
