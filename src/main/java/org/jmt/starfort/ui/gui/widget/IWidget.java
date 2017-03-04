package org.jmt.starfort.ui.gui.widget;

import java.util.ArrayList;

import org.jmt.starfort.ui.gui.NkCtxGLFW3;

public abstract class IWidget {

	public ArrayList<IWidget> children = new ArrayList<IWidget>(); 
	
	public ArrayList<IWidget> getWidgetChildren() {
		return children;
	}
	
	public void drawWidget(NkCtxGLFW3 jctx) {
		drawWidgetSelf(jctx);
		drawWidgetChildren(jctx);
	}
	
	public void drawWidgetChildren(NkCtxGLFW3 jctx) {
		for (IWidget child : children) {
			child.drawWidget(jctx);
		}
	}
	
	protected abstract void drawWidgetSelf(NkCtxGLFW3 jctx);
}
