package ua.mazik.javarune.shader;

import ua.mazik.delta.renderer.vertex.VertexFormat;
import ua.mazik.javarune.shader.data.TextureVertexData;

public class VertexFormats {
    public static final VertexFormat<TextureVertexData> TEXTURE = new VertexFormat<>(
            new VertexFormat.Attribute<>("position", VertexFormat.Type.VEC2, TextureVertexData::position),
            new VertexFormat.Attribute<>("uv", VertexFormat.Type.VEC2, TextureVertexData::uv),
            new VertexFormat.Attribute<>("pixel", VertexFormat.Type.PIXEL, TextureVertexData::pixel)
    );
}
