#version 410 core

#define SQUARE 0U
#define CIRCLE 1U
#define DONUT 2U

in vec2 center;
flat in uint type;
in vec4 color;
flat in uint centered;

out vec4 out_color;

void process_circle() {
    vec2 dist_b = center - vec2(0.5 * centered);
    float dist = dot(dist_b, dist_b);

    float radius = 0.5f;
    if (dist > (radius * radius)) discard;
}

void process_donut() {
    vec2 dist_b = center - vec2(0.5 * centered);
    float dist = dot(dist_b, dist_b);

    float radius = 0.5;
    float inner_radius = 0.25;
    if (dist >= (radius * radius) || dist <= (inner_radius * inner_radius)) discard;
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