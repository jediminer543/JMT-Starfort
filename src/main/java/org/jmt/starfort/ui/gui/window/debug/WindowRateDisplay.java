package org.jmt.starfort.ui.gui.window.debug;

import static org.lwjgl.nuklear.Nuklear.*;

import java.text.DecimalFormat;

import org.jmt.starfort.Starfort;
import org.jmt.starfort.renderer.Renderer;
import org.jmt.starfort.ui.gui.NkCtxGLFW3;
import org.jmt.starfort.ui.gui.widget.IWidget;
import org.jmt.starfort.world.TickRequest;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;

public class WindowRateDisplay implements IWidget {

	public boolean show = true;
	
	public Renderer framerateSource;
	public TickRequest tickrateSource; //failover
	
	private int winWidth = 100, winHeight = 50;
	
	public WindowRateDisplay(Renderer framerateSource, TickRequest tickrateSource) {
		this.framerateSource = framerateSource;
		this.tickrateSource = tickrateSource;
	}

	private DecimalFormat df = new DecimalFormat("#####.###");
	
	@Override
	public void drawWidget(NkCtxGLFW3 jctx) {
		NkRect bounds = NkRect.mallocStack(jctx.stack);
		NkContext ctx = jctx.ctx;
		if (show) {
			if (nk_begin(ctx, "Framerate", nk_rect(Starfort.winWidth-winWidth, 0, winWidth, winHeight, bounds), 
					NK_WINDOW_NO_SCROLLBAR | NK_WINDOW_NO_INPUT)) {
				nk_layout_row_dynamic(jctx.ctx, 20, 1);
				if (framerateSource != null) nk_label(ctx, df.format(framerateSource.FPS) + "  fps", NK_TEXT_ALIGN_RIGHT);
				if (framerateSource.lastRenderedWorld != null && framerateSource.lastRenderedWorld.getTickRequest() != null) 
					nk_label(ctx, df.format(framerateSource.lastRenderedWorld.getTickRequest().TPS) + " tps", NK_TEXT_ALIGN_RIGHT);
				else if (tickrateSource != null) nk_label(ctx, df.format(tickrateSource.TPS) + " tps", NK_TEXT_ALIGN_RIGHT);
			}
			nk_end(ctx);
		}
	}

	public Renderer getFramerateSource() {
		return framerateSource;
	}

	public void setFramerateSource(Renderer framerateSource) {
		this.framerateSource = framerateSource;
	}

	public TickRequest getTickrateSource() {
		return tickrateSource;
	}

	public void setTickrateSource(TickRequest tickrateSource) {
		this.tickrateSource = tickrateSource;
	}

}
