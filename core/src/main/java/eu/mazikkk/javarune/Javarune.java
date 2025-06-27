package eu.mazikkk.javarune;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Javarune implements ApplicationListener {
    private Texture krisDown;
    private Texture krisLeft;
    private Texture krisRight;
    private Texture krisUp;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private ShapeRenderer shapeRenderer;

    private Texture kris;
    private int x = 320 / 2 - 38 / 2;
    private int y = 240 / 2 - 19 / 2;

    @Override
    public void create() {
        krisDown = new Texture("kris_down.png");
        krisLeft = new Texture("kris_left.png");
        krisRight = new Texture("kris_right.png");
        krisUp = new Texture("kris_up.png");

        kris = krisDown;

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(320, 240);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            kris = krisUp;
            y += (int) (1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            kris = krisDown;
            y -= (int) (1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            kris = krisLeft;
            x -= (int) (1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            kris = krisRight;
            x += (int) (1);
        }

        ScreenUtils.clear(Color.BLACK);

        viewport.apply();

        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        shapeRenderer.end();

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        spriteBatch.draw(kris, x, y);

        spriteBatch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        krisDown.dispose();
        krisLeft.dispose();
        krisRight.dispose();
        krisUp.dispose();
        kris.dispose();
        spriteBatch.dispose();
        shapeRenderer.dispose();
    }
}
