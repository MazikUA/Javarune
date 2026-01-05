package ua.mazik.delta.renderer.draw;

import ua.mazik.delta.renderer.Renderer;
import ua.mazik.delta.renderer.Shader;
import ua.mazik.delta.renderer.Texture;
import ua.mazik.delta.renderer.VertexBuilder;
import ua.mazik.delta.renderer.attribute.FloatAttribute;
import ua.mazik.delta.util.Pixel;

public record TextureDrawElement(Texture texture, Shader shader, int x, int y, int u0, int v0, int u1, int v1, int width, int height, Pixel topLeftColor, Pixel topRightColor, Pixel bottomRightColor, Pixel bottomLeftColor) implements DrawElement {
    @Override
    public void build(VertexBuilder builder) {
        builder.vertex(new FloatAttribute(this.x, this.y), new FloatAttribute((float) this.u0 / texture.width, (float) this.v0 / texture.height), new FloatAttribute(topLeftColor.floatArray()));
        builder.vertex(new FloatAttribute(this.x + this.width, this.y), new FloatAttribute((float) this.u1 / texture.width, (float) this.v0 / texture.height), new FloatAttribute(topRightColor.floatArray()));
        builder.vertex(new FloatAttribute(this.x + this.width, this.y + this.height), new FloatAttribute((float) this.u1 / texture.width, (float) this.v1 / texture.height), new FloatAttribute(bottomRightColor.floatArray()));
        builder.vertex(new FloatAttribute(this.x, this.y + this.height), new FloatAttribute((float) this.u0 / texture.width, (float) this.v1 / texture.height), new FloatAttribute(bottomLeftColor.floatArray()));

        builder.triangle(0, 1, 3);
        builder.triangle(1, 2, 3);

        builder.nextOffset(4);
    }

    @Override
    public boolean isDirty(DrawElement previous) {
        if (previous instanceof TextureDrawElement drawElement) {
            return drawElement.texture != this.texture || drawElement.shader != this.shader;
        }
        return true;
    }

    @Override
    public void bind() {
        this.texture.bind();
        this.shader.bind();

        Renderer.applyDefaultUniforms(this.shader);
    }

    @Override
    public void unbind() {
        this.texture.unbind();
        this.shader.unbind();
    }
}
