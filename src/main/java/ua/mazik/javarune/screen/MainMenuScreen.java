package ua.mazik.javarune.screen;

import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.screen.element.OptionList;
import ua.mazik.javarune.text.Text;
import ua.mazik.javarune.text.font.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.lwjgl.sdl.SDLKeycode.*;

public class MainMenuScreen extends Screen {
    public final OptionList list;

    public MainMenuScreen() {
        List<OptionList.Option> options = new ArrayList<>();

        for (Map.Entry<String, String> entry : Javarune.languageManager().languageNames.entrySet()) {
            options.add(new OptionList.Option(Text.literal(entry.getValue()).overrides(Font.Condition.LANGUAGES, List.of(entry.getKey()))));
        }

        this.list = new OptionList(40, 100, this, options, 16, false);

        this.focusedElement = this.list;
    }

    @Override
    public void onKeyDown(int keycode, boolean repeat) {
        if (repeat) return;

        if (keycode == SDLK_UP) {
            this.list.previous();
        } else if (keycode == SDLK_DOWN) {
            this.list.next();
        }
    }

    @Override
    public void render() {
        this.list.render();
    }
}
