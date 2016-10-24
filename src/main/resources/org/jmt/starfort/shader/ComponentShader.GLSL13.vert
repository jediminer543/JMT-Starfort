#version 130

in vec2 v_ver;
in vec2 v_tex;
in vec4 v_col;

uniform int u_depth;
uniform mat4 u_projection;
uniform mat4 u_modelview;

out vec2 f_tex;
out vec4 f_col;

void main(void) {
	f_tex = v_tex;
	f_col = v_col;
	
	gl_Position = u_projection * u_modelview * vec4(v_ver, -u_depth, 1.0);
}