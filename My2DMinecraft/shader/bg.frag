#version 330 core

layout (location = 0) out vec4 color;

in DATA
{
	vec2 tc;
	vec3 position;
} fs_in;

uniform sampler2D tex;

void main()
{
	color = vec4((1.0 / 255.0) * 5.0, (1.0 / 255.0) * 19.0, (1.0 / 255.0) * 33.0, 1.0);
	//color = vec4(1.0, 1.0, 1.0, 1.0);
}