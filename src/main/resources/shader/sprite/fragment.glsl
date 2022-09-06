#version 410 core

in vec2 textureCoords;
flat in uint textureIndex;
in vec4 color;
flat in float colorPercentage;

uniform sampler2D in_samplers[100];

out vec4 out_color;

void main()
{
    vec4 textureColor = texture(in_samplers[textureIndex], textureCoords);
    if(textureColor.a == 0f) discard;
    out_color = mix(textureColor, color, colorPercentage);
};