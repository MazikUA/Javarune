package ua.mazik.javarune.screen;

import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.language.Language;
import ua.mazik.javarune.screen.element.OptionList;

import java.util.ArrayList;
import java.util.List;

public class MainMenuScreen extends Screen {
    public OptionList list;
    public OptionList list2;

    public MainMenuScreen() {
        List<OptionList.Option> options2 = new ArrayList<>();

        for (Language language : Javarune.languageManager().availableLanguages.values()) {
            options2.add(new OptionList.Option(language.nameText(), () -> {
                this.list.focus();
            }));
        }

        this.list2 = new OptionList(300, 100, this, options2, 16, true);

        List<OptionList.Option> options = new ArrayList<>();

        for (Language language : Javarune.languageManager().availableLanguages.values()) {
            options.add(new OptionList.Option(language.nameText(), this.list2::focus));
        }

        this.list = new OptionList(40, 100, this, options, 16, true);
        this.list.focus();
    }

    @Override
    public void render() {
        this.list.render();
        this.list2.render();
    }
}
