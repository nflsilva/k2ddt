#version 410 core

#define SQUARE 0U
#define CIRCLE 1U
#define DONUT 2U

in vec2 center;
flat in uint type;
in vec4 color;

out vec4 out_color;

void process_circle() {
    float radius = 0.5f;
    float dist = dot(center - 0.5, center - 0.5);
    if (dist > (radius * radius)){ discard; }
}

void process_donut() {
    float radius = 1.0;
    float inner_radius = 0.25;
    float dist = sqrt(dot(center, center));
    if (dist >= radius || dist <= inner_radius)
    { discard; }
}

void main()
{

    switch (type){
        case SQUARE: {
            // does nothing
            break;
        }
        case CIRCLE: {
            process_circle();
            break;
        }
        case DONUT: {
            process_donut();
            break;
        }
    }
    out_color = color;

};