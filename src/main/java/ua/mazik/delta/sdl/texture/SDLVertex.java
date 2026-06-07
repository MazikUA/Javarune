package ua.mazik.delta.sdl.texture;

import org.joml.Vector2f;
import org.lwjgl.sdl.*;
import ua.mazik.delta.util.Pixel;

public final class SDLVertex {
    public final Vector2f position = new Vector2f();
    public final Vector2f texCoord = new Vector2f();

    public Pixel color = Pixel.WHITE;

    public void apply(SDL_Vertex sdlVertex) {
        sdlVertex.position$().set(this.position.x, this.position.y);
        sdlVertex.color().set(this.color.red() / 255f, this.color.green() / 255f, this.color.blue() / 255f, this.color.alpha() / 255f);
        sdlVertex.tex_coord().set(this.texCoord.x, this.texCoord.y);
    }
}
