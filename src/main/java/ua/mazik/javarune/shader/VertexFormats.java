package ua.mazik.javarune.shader;

import ua.mazik.delta.renderer.vertex.VertexFormat;
import ua.mazik.delta.renderer.vertex.VertexType;
import ua.mazik.javarune.shader.data.TextureVertexData;

public class VertexFormats {
    public static final VertexFormat<TextureVertexData> TEXTURE = new VertexFormat.Builder<TextureVertexData>()
            .attribute("position", VertexType.VEC2, TextureVertexData::position)
            .attribute("uv", VertexType.VEC2, TextureVertexData::uv)
            .attribute("pixel", VertexType.PIXEL, TextureVertexData::pixel)
            .shader(Shaders.TEXTURE)
            .blend(true)
            .build();
}
