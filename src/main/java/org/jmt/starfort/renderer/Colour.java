package org.jmt.starfort.renderer;

import org.lwjgl.opengl.GL11;

public class Colour {
	
	public float r, g, b, a;
	
	public Colour(byte r, byte g, byte b, byte a) {
		set(r, g, b, a);
	}
	
	public Colour(float r, float g, float b, float a) {
		set(r, g, b, a);
	}
	
	public void set(byte r, byte g, byte b) {
		set(r, g, b, (byte) 255);
	}

	public void set(byte r, byte g, byte b, byte a) {
 		this.r = r/255;
 		this.g = g/255;
 		this.b = b/255;
 		this.a = a/255;
	}
	
	public void set(float r, float g, float b) {
		set(r, g, b, (float) 1f);
	}

	public void set(float r, float g, float b, float a) {
 		this.r = r;
 		this.g = g;
 		this.b = b;
 		this.a = a;
	}
	
	public void apply() {
		GL11.glColor4f(r, g, b, a);
	}
}
