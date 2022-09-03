#version 410 core

layout (location=0) in vec3 in_position;
layout (location=1) in float in_size;
layout (location=2) in int in_type;
layout (location=3) in vec4 in_color;

uniform mat4 in_projectionMatrix;

flat out int type;
out vec4 color;

void main(){
    type = in_type;
    color = in_color;
    gl_Position = in_projectionMatrix * vec4(in_position.xy, -in_position.z, 1f);
    gl_PointSize = in_size;
};