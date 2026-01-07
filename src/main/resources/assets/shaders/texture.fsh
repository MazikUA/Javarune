#version 330

uniform sampler2D tex;

in vec2 texCoordVarying;
in vec4 vertexColor;
out vec4 color;

void main() {
    color = texture(tex, texCoordVarying) * vertexColor;
}
