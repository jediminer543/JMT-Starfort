package org.jmt.starfort.event.events;

import org.jmt.starfort.event.IEventUI;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;

/**
 * Passes the world coord of a click event
 * 
 * Usefull for world level interaction.
 * 
 * @author jediminer543
 *
 */
public class EventWorldClick implements IEventUI {

	boolean consumed;
	
	World w;
	Coord c;

	long window;
	
	public EventWorldClick(long window, World w, Coord c) {
		super();
		this.w = w;
		this.c = c;
		this.window = window;
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
	
	public World getEventWorld() {
		return w;
	}
	
	public Coord getEventCoord() {
		return c;
	}

}
