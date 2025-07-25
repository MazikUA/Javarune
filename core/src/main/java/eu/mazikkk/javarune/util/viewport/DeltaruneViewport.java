package eu.mazikkk.javarune.util.viewport;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

public class DeltaruneViewport extends Viewport {
    public DeltaruneViewport() {
        setCamera(new OrthographicCamera());
        setWorldSize(640, 480);
    }

    @Override
    public void update(int screenWidth, int screenHeight, boolean centerCamera) {
        int scale = Math.min(screenWidth / 320, screenHeight / 240);

        int viewportWidth = 320 * scale;
        int viewportHeight = 240 * scale;

        setScreenBounds((screenWidth - viewportWidth) / 2, (screenHeight - viewportHeight) / 2, viewportWidth, viewportHeight);
        apply(centerCamera);
    }
}
