package org.jmt.starfort.ui.gui.widget;

import static org.lwjgl.nuklear.Nuklear.*;
import org.jmt.starfort.ui.gui.NkCtxGLFW3;

public class WidgetButton extends IWidget {
	
	@FunctionalInterface
	static interface ButtonCallback {
		public void call(boolean b);
	}
	
	String label;
	boolean state;
	ButtonCallback callback;
	
	public WidgetButton(String label) {
		this(label, null);
	}
	
	public WidgetButton(String label, ButtonCallback callback) {
		this.label = label;
		this.callback = callback;
	}
	
	@Override
	public void drawWidgetSelf(NkCtxGLFW3 jctx) {
		boolean lastState = state;
		state = nk_button_label(jctx.ctx, label);
		if (state != lastState && callback != null) {
			callback.call(state);
		}
	}
	
	public boolean getWidgetButtonState() {
		return state;
	}
	
	public void setWidgetButtonCallback(ButtonCallback cb) {
		callback = cb;
	}

}
