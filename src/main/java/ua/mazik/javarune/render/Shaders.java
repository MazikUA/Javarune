package ua.mazik.javarune.render;

import ua.mazik.delta.renderer.Shader;
import ua.mazik.javarune.Javarune;

import java.util.Optional;
import java.util.function.Supplier;

public class Shaders {
    public static final Supplier<Optional<Shader>> TEXTURE = () -> Javarune.getInstance().shaderLoader.get("texture");
}
