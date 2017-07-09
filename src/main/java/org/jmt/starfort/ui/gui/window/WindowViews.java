package org.jmt.starfort.ui.gui.window;

import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_BORDER;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_SCROLLBAR;
import static org.lwjgl.nuklear.Nuklear.nk_begin;
import static org.lwjgl.nuklear.Nuklear.nk_end;
import static org.lwjgl.nuklear.Nuklear.nk_rect;

import org.jmt.starfort.Starfort;
import org.jmt.starfort.ui.gui.NkCtxGLFW3;
import org.jmt.starfort.ui.gui.widget.IWidget;
import org.lwjgl.nuklear.NkRect;

public class WindowViews implements IWidget  {

	@Override
	public void drawWidget(NkCtxGLFW3 jctx) {
		NkRect bounds = NkRect.mallocStack(jctx.stack);
		nk_begin(jctx.ctx, "context", nk_rect(Starfort.winWidth/2 - 90, Starfort.winHeight-75, 180, 30, bounds), NK_WINDOW_BORDER | NK_WINDOW_NO_SCROLLBAR);
		//nk_layout_row_dynamic(jctx.ctx, 20, 3);
		nk_end(jctx.ctx);
	}
}
