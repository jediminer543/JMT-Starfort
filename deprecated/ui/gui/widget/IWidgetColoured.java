package org.jmt.starfort.ui.gui.widget;

import org.jmt.starfort.ui.gui.util.GUIColour;

public interface IWidgetColoured {

	public GUIColour getColour(String colourID);
	
	public void setColour(String colourID, GUIColour colour);
}
