package eu.mazikkk.javarune.draw;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eu.mazikkk.javarune.Javarune;
import eu.mazikkk.javarune.font.Font;

public record DrawContext(SpriteBatch spriteBatch) {
    public void drawTexture(String path, float x, float y, float width, float height) {
        this.spriteBatch.draw(Javarune.textureLoader.get(path), x, y, width, height);
    }

    public void drawRegion(TextureRegion region, float x, float y) {
        this.spriteBatch.draw(region, x, y);
    }

    public void drawText(String text, float x, float y, String fontId) {
        Font font = Javarune.fontLoader.get(fontId);

        font.draw(this, text, x, y);
    }
}
