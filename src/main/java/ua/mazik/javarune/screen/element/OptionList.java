package ua.mazik.javarune.screen.element;

import ua.mazik.delta.util.Pixel;
import ua.mazik.javarune.assets.AssetHelper;
import ua.mazik.javarune.screen.Screen;
import ua.mazik.javarune.text.Text;
import ua.mazik.javarune.text.TextRenderer;

import java.util.Collections;
import java.util.List;

import static org.lwjgl.sdl.SDLKeycode.*;

public class OptionList extends FocusableElement {
    public final List<Option> options;
    public final int gap;
    public final boolean cycled;

    private int selectedOption;

    public OptionList(int x, int y, Screen screen, List<Option> options, int gap, boolean cycled) {
        super(x, y, screen);

        this.options = Collections.unmodifiableList(options);
        this.gap = gap;
        this.cycled = cycled;
    }

    @Override
    public void render() {
        int y = this.y;

        for (int i = 0; i < options.size(); i++) {
            Text label = options.get(i).label;

            if (i == this.selectedOption && this.isFocused()) {
                label = label.copy().color(Pixel.YELLOW);

                AssetHelper.texture("gui/heart_small").draw(x - 30, y + 8, 18, 18);
            }

            TextRenderer.render(label, this.x, y);

            y += 32 + this.gap;
        }
    }

    public void next() {
        if (this.cycled) {
            this.selectedOption = (this.selectedOption + 1) % options.size();
        } else {
            this.selectedOption = Math.min(this.selectedOption + 1, options.size() - 1);
        }
    }

    public void previous() {
        if (this.cycled) {
            this.selectedOption = (this.selectedOption - 1 + options.size()) % options.size();
        } else {
            this.selectedOption = Math.max(this.selectedOption - 1, 0);
        }
    }

    @Override
    public void onKeyDown(int keycode, boolean repeat) {
        if (repeat) return;

        if (keycode == SDLK_UP) {
            this.previous();
            AssetHelper.playSound("menumove");
        } else if (keycode == SDLK_DOWN) {
            this.next();
            AssetHelper.playSound("menumove");
        } else if (keycode == SDLK_Z) {
            this.options.get(this.selectedOption).runnable.run();
            AssetHelper.playSound("select");
        }
    }

    public record Option(Text label, Runnable runnable) {
    }
}
