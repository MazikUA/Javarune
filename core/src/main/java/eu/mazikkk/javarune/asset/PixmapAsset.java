package eu.mazikkk.javarune.asset;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Disposable;

public record PixmapAsset(Pixmap pixmap, String path) implements Disposable {
    @Override
    public void dispose() {
        this.pixmap.dispose();
    }
}
