package ua.mazik.javarune.screen;

import org.jspecify.annotations.Nullable;
import ua.mazik.javarune.screen.element.FocusableElement;

public abstract class Screen implements Renderable {
    @Nullable
    public FocusableElement focusedElement;

    public void onKeyDown(int keycode, boolean repeat) {
        if (this.focusedElement != null) {
            this.focusedElement.onKeyDown(keycode, repeat);
        }
    }

    public void onKeyUp(int keycode, boolean repeat) {
        if (this.focusedElement != null) {
            this.focusedElement.onKeyUp(keycode, repeat);
        }
    }
}
