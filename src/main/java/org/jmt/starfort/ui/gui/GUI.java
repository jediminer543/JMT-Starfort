package org.jmt.starfort.ui.gui;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.jmt.starfort.ui.gui.widget.IWidget;
import org.lwjgl.nuklear.NkUserFont;
import org.lwjgl.nuklear.Nuklear;
import org.lwjgl.system.MemoryStack;

public class GUI {
	
	static NkCtxGLFW3 context = new NkCtxGLFW3();
	static ArrayList<IWidget> widgets = new ArrayList<>();
	
	public static void init(long window) {
		NkUserFont default_font = null;
		try {
			ByteBuffer font = NuklearUtil.StreamToByteBuffer("".getClass().getResourceAsStream("/org/jmt/starfort/font/Roboto-Regular.ttf"));
			default_font = NuklearUtil.nk_jmt_loadfont(font);
		} catch (IOException e) {
			e.printStackTrace();
		}
		NuklearUtil.nk_glfw3_init(context, window);
		context.default_font = default_font;
		Nuklear.nk_style_set_font(context.ctx, context.default_font);
	}

	public static void addWidget(IWidget widget) {
		widgets.add(widget);
	}
	
	public static void draw() {
			context.stack = MemoryStack.stackPush();
			NuklearUtil.nk_glfw3_new_frame(context);
			for (IWidget child : widgets) {
				child.drawWidget(context);
			}
			NuklearUtil.nk_glfw3_render(context, Nuklear.NK_ANTI_ALIASING_ON);
			context.stack.close();
	}
	
}
