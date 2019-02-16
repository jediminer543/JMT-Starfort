#version 130

in vec2 f_tex;

const int MODE_Colour = 1 << 0;
const int MODE_Depth = 1 << 1;
const int MODE_Mask = 1 << 2;

uniform int u_flags;

uniform vec4 u_col;
uniform int u_depth;
uniform vec4 u_depthCol;
uniform sampler2D u_tex;
uniform sampler2D u_mask;


out vec4 o_col;

void main(void) {
	o_col = texture(u_tex, f_tex);
	if ((u_flags & MODE_Colour) != 0) {
		if ((u_flags & MODE_Mask) != 0 && textureSize(u_tex, 0) == textureSize(u_mask, 0)) {
			if (texture(u_mask, f_tex).r == 1.0 && texture(u_mask, f_tex).g == 1.0 && texture(u_mask, f_tex).b == 1.0) {
				o_col = o_col * u_col;
			} //ELSE NOTHING
		} else {
			o_col = o_col * u_col;
		}
	}
	if ((u_flags & MODE_Depth) != 0 && u_depth > 0 && o_col.w > 0) {
		o_col = mix(o_col, u_depthCol, float(u_depth)/5) ; //TODO: FIGURE OUT WHAT TODO: implement u_depthCol  MEANS
	}
}
