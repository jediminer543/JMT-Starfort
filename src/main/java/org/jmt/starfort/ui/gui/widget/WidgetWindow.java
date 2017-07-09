package org.jmt.starfort.ui.gui.widget;

import static org.lwjgl.nuklear.Nuklear.*;
import org.jmt.starfort.ui.gui.NkCtxGLFW3;
import org.lwjgl.nuklear.NkRect;

public class WidgetWindow extends IWidgetTree {

	String title;
	int xpos, ypos, width, height;
	
	public WidgetWindow(String title, int xpos, int ypos, int width, int height) {
		this.title = title;
		this.xpos = xpos;
		this.ypos = ypos;
		this.width = width;
		this.height = height;
	}

	@Override
	public void drawWidget(NkCtxGLFW3 jctx) {
		drawWidgetSelf(jctx);
		nk_end(jctx.ctx);
	}
	
	@Override
	public void drawWidgetSelf(NkCtxGLFW3 jctx) {
		NkRect bounds = NkRect.mallocStack(jctx.stack);
		if (nk_begin(jctx.ctx, title, nk_rect(xpos, ypos, width, height, bounds), NK_WINDOW_BORDER | NK_WINDOW_TITLE | NK_WINDOW_MINIMIZABLE | NK_WINDOW_MOVABLE)) {
			drawWidgetChildren(jctx);
		}
	}
	
}
