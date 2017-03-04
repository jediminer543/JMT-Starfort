package org.jmt.starfort.event.events.ui;

import org.jmt.starfort.event.IEventUI;

public class EventCursorPos implements IEventUI {

	boolean consumed = false;
	
	long window;
	double xpos, ypos;
	
	public EventCursorPos(long window, double xpos, double ypos) {
		this.window = window;
		this.xpos = xpos;
		this.ypos = ypos;
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

	public double getEventXPos() {
		return xpos;
	}

	public double getEventYPos() {
		return ypos;
	}
	
}
