package ua.mazik.javarune.asset.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.jspecify.annotations.NonNull;
import ua.mazik.javarune.asset.AssetLoader;
import ua.mazik.javarune.asset.AssetManager;
import ua.mazik.javarune.font.Font;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FontLoader extends AssetLoader<Font> {
    public final Map<String, Font> fonts = new HashMap<>();

    public FontLoader(AssetManager assetManager) {
        super("fonts", assetManager);
    }

    @Override
    public void load() {
        this.close();
    }

    @NonNull
    @Override
    public Font get(@NonNull String path) {
        Font font = this.fonts.get(path);

        if (font == null) {
            Optional<InputStream> asset = this.assetManager.findAsset("fonts/" + path + ".json");

            if (asset.isPresent()) {
                try (InputStream in = asset.get()) {
                    byte[] bytes = in.readAllBytes();
                    JsonElement json = JsonParser.parseString(new String(bytes));
                    Font newFont = Font.CODEC.decode(json);
                    this.fonts.put(path, newFont);
                    return newFont;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return this.get("default");
        } else {
            return font;
        }
    }

    @Override
    public void close() {
        this.fonts.values().forEach(Font::close);
        this.fonts.clear();
    }
}
