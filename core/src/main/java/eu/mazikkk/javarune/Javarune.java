package eu.mazikkk.javarune;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import eu.mazikkk.javarune.asset.AssetManager;
import eu.mazikkk.javarune.asset.loader.TextureLoader;
import eu.mazikkk.javarune.asset.source.JarAssetSource;
import eu.mazikkk.javarune.util.viewport.DeltaruneViewport;

public class Javarune extends ApplicationAdapter {
    private DeltaruneViewport viewport;
    private Texture tex;
    private SpriteBatch batch;

    private AssetManager assetManager;
    private TextureLoader textureLoader;

    @Override
    public void create() {
        // TODO: make real tests
        // CodecTests.run();
        // DispatchTests.run();

        viewport = new DeltaruneViewport();
        batch = new SpriteBatch();

        this.assetManager = new AssetManager();
        this.textureLoader = new TextureLoader(this.assetManager);
        this.assetManager.registerLoader(this.textureLoader);
        this.assetManager.registerSource(new JarAssetSource(Javarune.class));
        this.assetManager.load();

        tex = textureLoader.get("font/default/ascii.png");
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height, true);
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);

        this.viewport.apply();

        batch.begin();
        batch.draw(tex, 0f, 0f);
        batch.end();
    }

    @Override
    public void dispose() {
        this.assetManager.dispose();
        this.tex.dispose();
    }
}
