package org.jmt.starfort.ui.gui.widget;

public abstract class Widget {
	
	/**
	 * Widget coordinates, stored as ints, to improve calculations
	 */
	float x, y;
	
	/**
	 * The width and height of the component, stored in 
	 */
	float width, height;
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setPos(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	public abstract void draw(long ctx);

}
