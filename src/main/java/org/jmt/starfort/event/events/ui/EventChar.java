package org.jmt.starfort.event.events.ui;

import org.jmt.starfort.event.IEventUI;

public class EventChar implements IEventUI {

	boolean consumed = false;
	
	long window;
	int codepoint;
	
	public EventChar(long window, int codepoint) {
		this.window = window;
		this.codepoint = codepoint;
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

	public int getEventCodepoint() {
		return codepoint;
	}
}
