package org.jmt.starfort.ui.gui;

import java.util.ArrayList;

import org.jmt.starfort.ui.UIEvent;
import org.joml.Vector3f;

public interface IGUIWidget {
	
	/**
	 * Get the widget's parent, or null if widget's parent is GUI
	 * @return the widget's parent
	 */
	public IGUIWidget getWidgetPatent();
	
	public void setWidgetPos(Vector3f newPos);
	
	/**
	 * Get the GUI element's relative position (positions are reletive to parent component)
	 * @return
	 */
	public Vector3f getWidgetPos();
	
	/**
	 * Called by GUI when widget is slected
	 * @return whether element was selected or not (return false to stop selection of component)
	 */
	boolean select();
	
	/**
	 * Get the child widgets
	 * @return
	 */
	public ArrayList<IGUIWidget> getWidgetChildren();
	
	/**
	 * Draws this component and all clild components
	 */
	public void drawWidget();
	
	/**
	 * Handle passed event4; will get more events passed when selected (if component can be selected
	 * @param gev
	 */
	public void handleEvent(UIEvent gev);
}
