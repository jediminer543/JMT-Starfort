package org.jmt.starfort.ui.gui.widget;

import java.util.ArrayList;

import org.jmt.starfort.ui.gui.NkCtxGLFW3;

public abstract class IWidgetTree implements IWidget {

	public ArrayList<IWidgetTree> children = new ArrayList<IWidgetTree>(); 
	
	public ArrayList<IWidgetTree> getWidgetChildren() {
		return children;
	}
	
	@Override
	public void drawWidget(NkCtxGLFW3 jctx) {
		drawWidgetSelf(jctx);
		drawWidgetChildren(jctx);
	}
	
	public void drawWidgetChildren(NkCtxGLFW3 jctx) {
		for (IWidgetTree child : children) {
			child.drawWidget(jctx);
		}
	}
	
	protected abstract void drawWidgetSelf(NkCtxGLFW3 jctx);
}
