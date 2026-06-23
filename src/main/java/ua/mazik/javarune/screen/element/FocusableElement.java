package ua.mazik.javarune.screen.element;

import ua.mazik.javarune.screen.Screen;

public abstract class FocusableElement extends Element {
    public FocusableElement(int x, int y, Screen screen) {
        super(x, y, screen);
    }

    public void focus() {
        this.screen.focusedElement = this;
    }

    public boolean isFocused() {
        return this.screen.focusedElement == this;
    }

    public void onKeyDown(int keycode, boolean repeat) {
    }

    public void onKeyUp(int keycode, boolean repeat) {
    }
}
