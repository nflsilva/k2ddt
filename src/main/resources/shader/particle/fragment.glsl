#version 410 core

flat in uint type;
in vec4 color;

out vec4 out_color;

void process_circle() {
    vec2 dist_b = gl_PointCoord - vec2(0.5);
    float dist = dot(dist_b, dist_b);
    float radius = 0.5;
    if (dist >= (radius * radius)) discard;
}

void process_other() {
    if (gl_PointCoord.x > 0.5 && gl_PointCoord.y > 0.5) discard;
}

void main()
{
    switch (type){
        case 0: {
            process_circle();
            break;
        }
        case 1: {
            process_other();
            break;
        }
    }

    out_color = color;
};