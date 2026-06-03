package ua.mazik.javarune.shader.data;

import org.joml.Vector2f;
import ua.mazik.delta.util.Pixel;

public record TextureVertexData(float x, float y, float u, float v, Pixel pixel) {
    public Vector2f position() {
        return new Vector2f(this.x, this.y);
    }

    public Vector2f uv() {
        return new Vector2f(this.u, this.v);
    }
}
