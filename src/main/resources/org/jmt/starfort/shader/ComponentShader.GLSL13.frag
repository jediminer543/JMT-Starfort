#version 130

in vec2 f_tex;
in vec4 f_col;

uniform int u_depth;
uniform vec4 u_depthCol;
uniform sampler2D u_tex;

out vec4 o_col;

void main(void) {

	o_col = f_col * (u_depth * u_depthCol) * texture(u_tex, f_tex);
}