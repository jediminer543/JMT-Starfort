package org.jmt.starfort.ui.gui.widget;

import org.jmt.starfort.ui.gui.util.GUIColour;

public interface IWidgetBordered {

	public void setBorderColour(GUIColour c);
	
	public GUIColour getBorderColour();
	
	public void setBorderShow(boolean show);
	
	public boolean getBorderShow();
}
