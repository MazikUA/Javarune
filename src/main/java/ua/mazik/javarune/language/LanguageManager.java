package ua.mazik.javarune.language;

import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.text.Text;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class LanguageManager {
    public static final String DEFAULT_LANGUAGE = "en_us";

    public final Map<String, Language> availableLanguages;
    public final Language defaultLanguage;

    private Language currentLanguage;

    public LanguageManager() {
        Map<String, Language> map = new TreeMap<>();

        Javarune.assetSource().listAssets("langs", ".json").forEach(asset -> {
            map.put(asset.fileName(), new Language(asset));
        });

        this.availableLanguages = Collections.unmodifiableMap(map);
        this.defaultLanguage = map.get(DEFAULT_LANGUAGE);
        this.defaultLanguage.read();

        this.reloadLanguage();
    }

    public void reloadLanguage() {
        Language language = this.availableLanguages.get(Javarune.SETTINGS.language.get());

        if (this.currentLanguage != language) {
            if (this.currentLanguage != null) {
                this.currentLanguage.clear();
            }

            this.currentLanguage = language;
            this.currentLanguage.read();
        }
    }

    public Optional<Text> get(String key) {
        return this.currentLanguage.get(key).or(() -> this.defaultLanguage.get(key));
    }
}
