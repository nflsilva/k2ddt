#version 410 core

layout (location=0) in vec2 in_position;
layout (location=1) in vec4 in_color;

uniform mat4 in_projectionMatrix;

out vec4 color;

void main(){
    color = in_color;
    gl_Position = in_projectionMatrix * vec4(in_position.xy, 0f, 1f);
};