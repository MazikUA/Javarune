package ua.mazik.delta.sdl.texture;

import org.lwjgl.sdl.*;
import org.lwjgl.system.MemoryStack;

public class SDLQuad {
    public final SDLVertex topLeft = new SDLVertex();
    public final SDLVertex topRight = new SDLVertex();

    public final SDLVertex bottomRight = new SDLVertex();
    public final SDLVertex bottomLeft = new SDLVertex();

    private float x = 0;
    private float y = 0;

    private float w = 0;
    private float h = 0;

    public SDLQuad() {
        this.topRight.texCoord.x = 1f;

        this.bottomRight.texCoord.set(1f, 1f);

        this.bottomLeft.texCoord.y = 1f;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;

        this.update();
    }

    public void setSize(float w, float h) {
        this.w = w;
        this.h = h;

        this.update();
    }

    public void setTexCoord(float u, float v, float w, float h, float textureWidth, float textureHeight) {
        float u0 = u / textureWidth;
        float v0 = v / textureHeight;

        float u1 = (u + w) / textureWidth;
        float v1 = (v + h) / textureHeight;

        this.topLeft.texCoord.set(u0, v0);
        this.topRight.texCoord.set(u1, v0);
        this.bottomRight.texCoord.set(u1, v1);
        this.bottomLeft.texCoord.set(u0, v1);
    }

    public SDL_Vertex.Buffer buffer(MemoryStack stack) {
        SDL_Vertex.Buffer buf = SDL_Vertex.malloc(4, stack);

        this.topLeft.apply(buf.get(0));
        this.topRight.apply(buf.get(1));
        this.bottomRight.apply(buf.get(2));
        this.bottomLeft.apply(buf.get(3));

        return buf;
    }

    private void update() {
        this.topLeft.position.set(this.x, this.y);
        this.topRight.position.set(this.x + this.w, this.y);
        this.bottomRight.position.set(this.x + this.w, this.y + this.h);
        this.bottomLeft.position.set(this.x, this.y + this.h);
    }
}
