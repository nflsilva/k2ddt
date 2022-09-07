#version 410 core

layout (location=0) in vec2 in_position;
layout (location=1) in float in_size;
layout (location=2) in uint in_type;
layout (location=3) in vec4 in_color;
layout (location=4) in uint in_layer;

uniform mat4 in_projectionMatrix;

flat out uint type;
out vec4 color;

void main(){
    type = in_type;
    color = in_color;
    gl_Position = in_projectionMatrix * vec4(in_position.xy, in_layer * -1f, 1f);
    gl_PointSize = in_size;
};