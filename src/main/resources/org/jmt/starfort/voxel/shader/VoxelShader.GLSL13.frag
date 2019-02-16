#version 130

uniform vec4 u_col;
uniform int u_depth;
uniform vec4 u_depthCol;

out vec4 o_col;

void main(void) {
	o_col = texture(u_tex, f_tex);
	o_col = o_col * u_col;
	if (u_depth > 0 && o_col.w > 0) {
		o_col = mix(o_col, u_depthCol, float(u_depth)/5) ; //TODO: FIGURE OUT WHAT TODO: implement u_depthCol  MEANS
	}
}
