package eu.mazikkk.javarune;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import eu.mazikkk.javarune.test.CodecTests;

public class Javarune extends Game {
    private FitViewport viewport;

    @Override
    public void create() {
        CodecTests.run();

        viewport = new FitViewport(640, 480);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);

        viewport.apply();
    }
}
