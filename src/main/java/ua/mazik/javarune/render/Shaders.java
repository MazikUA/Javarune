package ua.mazik.javarune.render;

import ua.mazik.delta.renderer.Shader;
import ua.mazik.javarune.Javarune;

public class Shaders {
    public static final Shader.Getter TEXTURE = () -> Javarune.getInstance().shaderLoader.get("texture").orElseThrow();
}
