package ua.mazik.javarune.locale;

import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.text.Text;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class LocaleManager {
    public static final String DEFAULT_LOCALE = "en_us";

    public final Map<String, Locale> availableLocales;
    public final Locale defaultLocale;

    private Locale currentLocale;

    public LocaleManager() {
        Map<String, Locale> map = new TreeMap<>();

        Javarune.assetSource().listAssets("locales", ".json").forEach(asset -> {
            map.put(asset.fileName(), new Locale(asset));
        });

        this.availableLocales = Collections.unmodifiableMap(map);
        this.defaultLocale = map.get(DEFAULT_LOCALE);
        this.defaultLocale.read();

        this.reloadLocale();
    }

    public void reloadLocale() {
        Locale locale = this.availableLocales.get(Javarune.SETTINGS.locale.get());

        if (this.currentLocale != locale) {
            if (this.currentLocale != null) {
                this.currentLocale.clear();
            }

            this.currentLocale = locale;
            this.currentLocale.read();
        }
    }

    public Optional<Text> get(String key) {
        return this.currentLocale.get(key).or(() -> this.defaultLocale.get(key));
    }
}
