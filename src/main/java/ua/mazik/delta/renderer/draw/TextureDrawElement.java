package ua.mazik.delta.renderer.draw;

import ua.mazik.delta.renderer.Renderer;
import ua.mazik.delta.renderer.Shader;
import ua.mazik.delta.renderer.Texture;
import ua.mazik.delta.renderer.VertexBuilder;
import ua.mazik.delta.renderer.attribute.FloatAttribute;

public record TextureDrawElement(Texture texture, Shader shader, int x, int y, int u0, int v0, int u1, int v1,
                                 int width, int height) implements DrawElement {
    @Override
    public void build(VertexBuilder builder) {
        builder.vertex(new FloatAttribute(this.x, this.y), new FloatAttribute((float) this.u0 / texture.width, (float) this.v0 / texture.height));
        builder.vertex(new FloatAttribute(this.x + this.width, this.y), new FloatAttribute((float) this.u1 / texture.width, (float) this.v0 / texture.height));
        builder.vertex(new FloatAttribute(this.x + this.width, this.y + this.height), new FloatAttribute((float) this.u1 / texture.width, (float) this.v1 / texture.height));
        builder.vertex(new FloatAttribute(this.x, this.y + this.height), new FloatAttribute((float) this.u0 / texture.width, (float) this.v1 / texture.height));

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
