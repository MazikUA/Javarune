package eu.mazikkk.javarune;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import eu.mazikkk.javarune.asset.AssetManager;
import eu.mazikkk.javarune.asset.loader.FontLoader;
import eu.mazikkk.javarune.asset.loader.TextureLoader;
import eu.mazikkk.javarune.asset.source.JarAssetSource;
import eu.mazikkk.javarune.draw.DrawContext;
import eu.mazikkk.javarune.util.viewport.DeltaruneViewport;

public class Javarune extends ApplicationAdapter {
    public static DeltaruneViewport viewport;
    public static SpriteBatch batch;

    public static AssetManager assetManager;
    public static TextureLoader textureLoader;
    public static FontLoader fontLoader;

    @Override
    public void create() {
        viewport = new DeltaruneViewport();
        batch = new SpriteBatch();

        assetManager = new AssetManager();
        textureLoader = new TextureLoader(assetManager);
        assetManager.registerLoader(textureLoader);
        fontLoader = new FontLoader(assetManager);
        assetManager.registerLoader(fontLoader);
        assetManager.registerSource(new JarAssetSource(Javarune.class));
        assetManager.load();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);

        viewport.apply();
        batch.begin();

        DrawContext context = new DrawContext(batch);

        context.drawText(" !\"#$%&'()*+,-./", 10, 110, "default");
        context.drawText("0123456789:;<=>?", 10, 90, "default");
        context.drawText("@ABCDEFGHIJKLMNO", 10, 70, "default");
        context.drawText("PQRSTUVWXYZ[\\]^_", 10, 50, "default");
        context.drawText("`abcdefghijklmno", 10, 30, "default");
        context.drawText("pqrstuvwxyz{|}` ", 10, 10, "default");

        batch.end();
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
