#version 330

layout(location = 0) in vec2 position;
layout(location = 1) in vec2 texCoord;
layout(location = 2) in vec4 color;

out vec2 texCoordVarying;
out vec4 vertexColor;

uniform mat4 projection;

void main() {
    texCoordVarying = texCoord;
    vertexColor = color;
    gl_Position = projection * vec4(position, 0.0, 1.0);
}
