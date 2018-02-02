package org.jmt.starfort.event.ui;

import org.jmt.starfort.event.IEventUI;

public class EventMouseButton implements IEventUI {

	boolean consumed = false;
	
	long window;
	int button, action, mods;
	
	public EventMouseButton(long window, int button, int action, int mods) {
		this.window = window;
		this.button = button;
		this.action = action;
		this.mods = mods;
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

	public int getEventButton() {
		return button;
	}

	public int getEventAction() {
		return action;
	}

	public int getEventMods() {
		return mods;
	}

}
