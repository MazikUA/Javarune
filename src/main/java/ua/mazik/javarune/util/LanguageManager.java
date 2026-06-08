package ua.mazik.javarune.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ua.mazik.javarune.Javarune;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class LanguageManager {
    public static final String DEFAULT_LANGUAGE = "en_us";

    public final Map<String, String> languageNames;

    private final Map<String, String> defaultLang;
    private Map<String, String> lang;

    public LanguageManager() {
        Map<String, String> map = new TreeMap<>();

        Javarune.assetSource().listAssets("lang", ".json").forEach(asset -> {
            String langName = asset.fileName();

            JsonElement json = JsonParser.parseString(new String(asset.getBytes(), StandardCharsets.UTF_8));

            if (!json.isJsonObject()) throw new IllegalArgumentException();

            JsonObject obj = json.getAsJsonObject();
            JsonElement element = obj.get("lang.name");

            if (element == null || !element.isJsonPrimitive()) throw new IllegalArgumentException();

            map.put(langName, element.getAsString());
        });

        this.languageNames = Collections.unmodifiableMap(map);
        this.defaultLang = this.loadLanguage(DEFAULT_LANGUAGE);

        this.reloadLanguage();
    }

    public void reloadLanguage() {
        String langKey = Javarune.SETTINGS.language.get();

        if (langKey.equals(DEFAULT_LANGUAGE)) {
            this.lang = this.defaultLang;
        } else {
            this.lang = this.loadLanguage(Javarune.SETTINGS.language.get());
        }
    }

    public Optional<String> get(String key) {
        String value = this.lang.get(key);

        if (value == null) {
            value = this.defaultLang.get(key);
        }

        return Optional.ofNullable(value);
    }

    private Map<String, String> loadLanguage(String langKey) {
        Map<String, String> map = new HashMap<>();

        JsonElement json = Javarune.jsonLoader().get("lang/" + langKey).orElseThrow();

        if (!json.isJsonObject()) throw new IllegalArgumentException();

        for (Map.Entry<String, JsonElement> field : json.getAsJsonObject().entrySet()) {
            map.put(field.getKey(), field.getValue().getAsString());
        }

        return Collections.unmodifiableMap(map);
    }
}
