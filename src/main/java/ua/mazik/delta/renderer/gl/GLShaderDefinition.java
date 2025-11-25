package ua.mazik.delta.renderer.gl;

import ua.mazik.delta.renderer.ShaderDefinition;

public record GLShaderDefinition(String vertex, String fragment) implements ShaderDefinition {

}
