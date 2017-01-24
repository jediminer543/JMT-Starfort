package org.jmt.starfort.ui.gui.widget;

import java.util.ArrayList;

import org.jmt.starfort.ui.gui.NkCtxGLFW3;

public interface IWidget {

	public ArrayList<IWidget> children = new ArrayList<IWidget>(); 
	
	public default void draw(NkCtxGLFW3 jctx) {
		drawSelf(jctx);
		drawChildren(jctx);
	}
	
	public default void drawChildren(NkCtxGLFW3 jctx) {
		for (IWidget child : children) {
			
		}
	}
	
	public void drawSelf(NkCtxGLFW3 jctx);
}
