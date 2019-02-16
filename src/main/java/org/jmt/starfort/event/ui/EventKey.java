package org.jmt.starfort.event.ui;

/**
 * Fired whenever a key is pressed while the screen is selected
 * 
 * @author jediminer543
 *
 */
public class EventKey implements IEventUI {

	boolean consumed = false;
	
	long window;
	int key, scancode, action, mods;
	
	public EventKey(long window, int key, int scancode, int action, int mods) {
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
	public long getEventWindow() {
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
