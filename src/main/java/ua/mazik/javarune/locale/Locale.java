package ua.mazik.javarune.locale;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ua.mazik.delta.assets.Asset;
import ua.mazik.delta.util.Pixel;
import ua.mazik.javarune.text.Text;
import ua.mazik.javarune.text.font.Font;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Locale {
    public final String key;
    public final String name;
    public final int progress;

    private final Asset asset;
    private final Map<String, Text> fields;

    public Locale(Asset asset) {
        JsonObject obj = getJsonObject(asset);

        this.key = asset.fileName();
        this.name = obj.get("locale.name").getAsString();
        // TODO: implement
        this.progress = 0;

        this.asset = asset;
        this.fields = new HashMap<>();
    }

    private static Text parse(String content) {
        if (content.isEmpty()) return Text.literal(content);

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
                    Text text = Text.literal(builder.toString()).color(currentColor);

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
            Text text = Text.literal(builder.toString()).color(currentColor);

            if (root != null) {
                root.add(text);
            } else {
                root = text;
            }
        }

        if (root == null) {
            root = Text.literal("");
        }

        return root;
    }

    private static JsonObject getJsonObject(Asset asset) {
        JsonElement element = JsonParser.parseString(asset.getContent());

        if (!element.isJsonObject()) throw new IllegalArgumentException();

        return element.getAsJsonObject();
    }

    public void read() {
        this.fields.clear();

        JsonObject obj = getJsonObject(this.asset);

        for (Map.Entry<String, JsonElement> field : obj.entrySet()) {
            this.fields.put(field.getKey(), parse(field.getValue().getAsString()));
        }
    }

    public Optional<Text> get(String key) {
        return Optional.ofNullable(this.fields.get(key));
    }

    public void clear() {
        this.fields.clear();
    }

    public Text nameText() {
        return Text.literal(this.name).overrides(Font.Condition.LOCALES, List.of(this.key));
    }
}
