package org.jmt.starfort.ui.gui.util;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;

/**
 * Colour = color
 * 
 * This code is writen by a british person, and I reserve the right to name classes using whatever method I wish. It is also about 10:30,
 * so my tiredness has made me insane.
 * 
 * This class stores java colour and can be used to generate NVGLColor. All modifying functions return the class so that multiple 
 * 
 * @author Jediminer543
 * 
 * TODO
 *
 */
public class GUIColour {
	
	/**
	 * Colours, stored as bytes
	 */
	private byte r, b, g, a;
	
	/**
	 * Determines if the native colour must be regenerated
	 */
	boolean changed = true;
	
	NVGColor color;
	
	public GUIColour() {
		color = NVGColor.create();
	}
	
	
	public GUIColour setR(int r) {
		this.r = (byte) r;
		this.changed = true;
		return this;
	}
	
	public GUIColour setB(int b) {
		this.b = (byte) b;
		this.changed = true;
		return this;
	}
	
	public GUIColour setG(int g) {
		this.g = (byte) g;
		this.changed = true;
		return this;
	}
	
	public GUIColour setA(int a) {
		this.a = (byte) a;
		this.changed = true;
		return this;
	}
	
	public NVGColor getNativeColour() {
		if (changed) {
			NanoVG.nvgRGBA(r, g, b, a, color);
			changed = false;
		}
		return color;
	}
	
}
