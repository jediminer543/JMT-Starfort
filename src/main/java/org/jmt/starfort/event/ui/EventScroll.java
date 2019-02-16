package org.jmt.starfort.event.ui;

public class EventScroll implements IEventUI {

	boolean consumed = false;
	
	long window;
	double xoffset, yoffset;
	
	public EventScroll(long window, double xoffset, double yoffset) {
		this.window = window;
		this.xoffset = xoffset;
		this.yoffset = yoffset;
	}

	@Override
	public boolean getEventConsumed() {
		return consumed;
	}

	@Override
	public void consumeEvent() {
		consumed = true;
	}

	@Override
	public long getEventWindow() {
		return window;
	}

	public double getEventXoffset() {
		return xoffset;
	}

	public double getEventYoffset() {
		return yoffset;
	}
	
}
