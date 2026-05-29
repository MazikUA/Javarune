package ua.mazik.javarune.shader;

import ua.mazik.delta.Renderer;
import ua.mazik.delta.renderer.Shader;
import ua.mazik.delta.renderer.Texture;
import ua.mazik.delta.renderer.VertexBuilder;
import ua.mazik.delta.renderer.draw.DrawElement;
import ua.mazik.delta.renderer.vertex.VertexFormat;
import ua.mazik.delta.util.Pixel;
import ua.mazik.javarune.shader.data.TextureVertexData;

public record TextureDrawElement(Texture texture, Shader shader, int x, int y, int u0, int v0, int u1, int v1, int width, int height, Pixel topLeftColor, Pixel topRightColor, Pixel bottomRightColor, Pixel bottomLeftColor) implements DrawElement<TextureVertexData> {
    @Override
    public VertexFormat<TextureVertexData> format() {
        return VertexFormats.TEXTURE;
    }

    @Override
    public void build(VertexBuilder<TextureVertexData> builder) {
        builder.quad(
                new TextureVertexData(this.x, this.y, (float) this.u0 / texture.width, (float) this.v0 / texture.height, topLeftColor),
                new TextureVertexData(this.x + this.width, this.y, (float) this.u1 / texture.width, (float) this.v0 / texture.height, topRightColor),
                new TextureVertexData(this.x + this.width, this.y + this.height, (float) this.u1 / texture.width, (float) this.v1 / texture.height, bottomRightColor),
                new TextureVertexData(this.x, this.y + this.height, (float) this.u0 / texture.width, (float) this.v1 / texture.height, bottomLeftColor)
        );
    }

    @Override
    public boolean isDirty(DrawElement<?> previous) {
        if (previous instanceof TextureDrawElement drawElement) {
            return drawElement.texture != this.texture || drawElement.shader != this.shader;
        }
        return true;
    }

    @Override
    public void bind() {
        this.shader.bind();

        Renderer.applyDefaultUniforms(this.shader);
    }

    @Override
    public void unbind() {
        this.shader.unbind();
    }
}
