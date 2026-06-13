package ua.mazik.javarune.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ua.mazik.delta.util.Pixel;
import ua.mazik.javarune.Javarune;
import ua.mazik.javarune.text.Text;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class LanguageManager {
    public static final String DEFAULT_LANGUAGE = "en_us";

    public final Map<String, String> languageNames;

    private final Map<String, Text> defaultLang;
    private Map<String, Text> lang;

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

    private static Text parse(String content) {
        if (content.isEmpty()) return new Text(content);

        Text root = null;

        StringBuilder builder = new StringBuilder();
        Pixel currentColor = Pixel.WHITE;

        for (int i = 0; i < content.length(); i++) {
            char character = content.charAt(i);

            if (character == '\\' && i + 2 < content.length() && content.charAt(i + 1) == 'c') {
                char code = content.charAt(i + 2);

                Pixel color = switch (code) {
                    case 'R' -> Pixel.RED;
                    case 'B' -> Pixel.BLUE;
                    case 'Y' -> Pixel.YELLOW;
                    case 'G' -> Pixel.LIME;
                    case 'W' -> Pixel.WHITE;
                    case 'X' -> Pixel.BLACK;
                    case 'P' -> Pixel.PURPLE;
                    case 'M' -> Pixel.MAROON;
                    case 'O' -> Pixel.ORANGE;
                    case 'A' -> Pixel.AZURE;
                    case 'S' -> Pixel.MAGENTA;
                    case 'V' -> Pixel.VIRIDIAN;
                    case 'I' -> Pixel.ICE;
                    default -> null;
                };

                if (color == null) {
                    builder.append("\\c").append(code);
                    i += 2;

                    continue;
                }

                if (!builder.isEmpty()) {
                    Text text = new Text(builder.toString()).color(currentColor);

                    if (root != null) {
                        root.add(text);
                    } else {
                        root = text;
                    }

                    builder.setLength(0);
                }

                currentColor = color;

                i += 2;
                continue;
            }

            builder.append(character);
        }

        if (!builder.isEmpty()) {
            Text text = new Text(builder.toString()).color(currentColor);

            if (root != null) {
                root.add(text);
            } else {
                root = text;
            }
        }

        if (root == null) {
            root = new Text("");
        }

        return root;
    }

    public void reloadLanguage() {
        String langKey = Javarune.SETTINGS.language.get();

        if (langKey.equals(DEFAULT_LANGUAGE)) {
            this.lang = this.defaultLang;
        } else {
            this.lang = this.loadLanguage(Javarune.SETTINGS.language.get());
        }
    }

    public Optional<Text> get(String key) {
        Text value = this.lang.get(key);

        if (value == null) {
            value = this.defaultLang.get(key);
        }

        return Optional.ofNullable(value);
    }

    private Map<String, Text> loadLanguage(String langKey) {
        Map<String, Text> map = new HashMap<>();

        JsonElement json = Javarune.jsonLoader().get("lang/" + langKey).orElseThrow();

        if (!json.isJsonObject()) throw new IllegalArgumentException();

        for (Map.Entry<String, JsonElement> field : json.getAsJsonObject().entrySet()) {
            map.put(field.getKey(), parse(field.getValue().getAsString()));
        }

        return Collections.unmodifiableMap(map);
    }
}
