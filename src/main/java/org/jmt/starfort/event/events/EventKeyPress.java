package org.jmt.starfort.event.events;

import org.jmt.starfort.event.IEventUI;

public class EventKeyPress implements IEventUI {

	boolean consumed = false;
	public long window;
	public int key, scancode, action, mods;
	
	public EventKeyPress(long window, int key, int scancode, int action, int mods) {
		this.window = window;
		this.key = key;
		this.scancode = scancode;
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
	public long getEventUIWindow() {
		return window;
	}

	public int getEventKey() {
		return key;
	}

	public int getEventScancode() {
		return scancode;
	}

	public int getEventAction() {
		return action;
	}

	public int getEventMods() {
		return mods;
	}
	
	

}
