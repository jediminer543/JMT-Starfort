package org.jmt.starfort.ui.gui.widget;

import org.jmt.starfort.ui.gui.util.GUIColour;

public interface IWidgetBackgrounded {

	public void setBackgroundColour(GUIColour c);
	
	public GUIColour getBackgroundColour();
	
	public void setBackgroundShow(boolean show);
	
	public boolean getBackgroundShow();
	
}
