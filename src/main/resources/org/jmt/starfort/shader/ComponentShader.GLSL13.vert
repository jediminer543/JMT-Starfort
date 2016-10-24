#version 130

in vec2 v_ver;
in vec2 v_tex;

out vec2 f_tex;

uniform int u_depth;
uniform mat4 u_projection;
uniform mat4 u_modelview;

void main(void) {
	f_tex = v_tex;
	
	gl_Position = u_projection * u_modelview * vec4(v_ver, 0.0, 1.0);
}