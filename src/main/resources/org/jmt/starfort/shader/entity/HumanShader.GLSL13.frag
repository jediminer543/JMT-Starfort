#version 130

in vec2 f_tex;

uniform vec4 u_mapcol[6];
uniform vec4 u_dstcol[6];
uniform sampler2D u_tex;

out vec4 o_col;

void main(void) {
	vec4 f_texCol = texture(u_tex, f_tex);
	if (f_texCol.w > 0) {
		if (u_mapcol[0] == f_texCol) {
			o_col = u_dstcol[0];
		} else if (u_mapcol[1] == f_texCol) {
			o_col = u_dstcol[1];
		} else if (u_mapcol[2] == f_texCol) {
			o_col = u_dstcol[2];
		} else if (u_mapcol[3] == f_texCol) {
			o_col = u_dstcol[3];
		} else if (u_mapcol[4] == f_texCol) {
			o_col = u_dstcol[4];
		} else if (u_mapcol[5] == f_texCol) {
			o_col = u_dstcol[5];
		} else {
			o_col = f_texCol;
		}
	}
}