#version 130

in vec2 f_tex;

uniform vec4 u_col;
uniform int u_depth;
uniform vec4 u_depthCol;
uniform sampler2D u_tex;

out vec4 o_col;

void main(void) {

	o_col = u_col * texture(u_tex, f_tex);// * (u_depth * u_depthCol) ;
}