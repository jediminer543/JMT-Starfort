#version 130

in vec2 f_tex;

uniform vec4 u_col;
uniform int u_depth;
uniform vec4 u_depthCol;
uniform sampler2D u_tex;

out vec4 o_col;

void main(void) {
	vec4 f_texCol = texture(u_tex, f_tex);
	float u_depthf = float(u_depth);
	if (f_texCol.w > 0) {
		if (u_depth > 0) {
			o_col = mix(u_col * f_texCol, u_depthCol, u_depthf/5) ; //TODO: implement u_depthCol
		} else { 
			o_col = u_col * f_texCol;
		} 
	}
}