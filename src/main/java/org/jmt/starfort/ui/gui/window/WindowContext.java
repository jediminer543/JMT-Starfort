package org.jmt.starfort.ui.gui.window;

import static org.lwjgl.nuklear.Nuklear.*;

import org.jmt.starfort.Starfort;
import org.jmt.starfort.ui.gui.NkCtxGLFW3;
import org.jmt.starfort.ui.gui.widget.IWidget;
import org.lwjgl.nuklear.NkRect;

public class WindowContext implements IWidget {

	boolean lastleft, lastright;
	
	@Override
	public void drawWidget(NkCtxGLFW3 jctx) {
		NkRect bounds = NkRect.mallocStack(jctx.stack);
		nk_begin(jctx.ctx, "context", nk_rect(Starfort.winWidth/2 - 90, Starfort.winHeight-75, 180, 30, bounds), NK_WINDOW_BORDER | NK_WINDOW_NO_SCROLLBAR);
		nk_layout_row_dynamic(jctx.ctx, 20, 3);
		boolean left = nk_button_label(jctx.ctx, "<");
		if (left != lastleft) {
			System.out.println("<");
		}
		nk_label(jctx.ctx, "Build", NK_TEXT_CENTERED);
		boolean right = nk_button_label(jctx.ctx, ">");
		if (right != lastright) {
			System.out.println(">");
		}
		nk_end(jctx.ctx);
	}
	
}
