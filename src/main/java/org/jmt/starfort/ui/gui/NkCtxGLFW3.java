package org.jmt.starfort.ui.gui;

import org.lwjgl.nuklear.NkBuffer;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkDrawNullTexture;
import org.lwjgl.nuklear.NkUserFont;

public class NkCtxGLFW3 {
	public NkContext ctx = NkContext.create();
	public NkUserFont default_font = NkUserFont.create();
	
	public long win;
	
	public int prog, vert_shdr, frag_shdr;
	public int vbo, vao, ebo;
	public int width, height, display_width, display_height;
	
	public int uniform_tex, uniform_proj;
	
	public NkBuffer          cmds         = NkBuffer.create();
	public NkDrawNullTexture null_texture = NkDrawNullTexture.create();
}