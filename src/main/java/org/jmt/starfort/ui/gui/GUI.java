package org.jmt.starfort.ui.gui;

import java.util.ArrayList;

public class GUI {
	
	static ArrayList<IGUIWidget> children = new ArrayList<IGUIWidget>();
	static IGUIWidget selectedWidget = null; 
	
	/**
	 * Selects a widget to take inputs
	 * @param widget
	 */
	public static void selectGUIWiget(IGUIWidget widget) {
		//TODO
	}

	public static ArrayList<IGUIWidget> getGUIChildren() {
		return children;
		
	}
	
	public static void draw() {
		/*
		GL11.glPushMatrix();
		JMTGl.jglPushMatrix();
		GL11.glLoadIdentity();
		JMTGl.jglLoadIdentity();
		for (IGUIWidget w : children) {
			w.drawWidget();
		}
		GL11.glPopMatrix();
		JMTGl.jglPopMatrix();
		*/
	}
}
