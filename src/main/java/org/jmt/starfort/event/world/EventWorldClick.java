package org.jmt.starfort.event.world;

import org.jmt.starfort.event.ui.EventMouseButton;
import org.jmt.starfort.event.ui.IEventUI;
import org.jmt.starfort.renderer.Renderer;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;

/**
 * Passes the world coord of a click event
 * 
 * Useful for world level interaction.
 * 
 * These are translated by the renderer
 * 
 * Consumption of this event will also consume the triggering click event
 * 
 * @see Renderer
 * @see EventMouseButton
 * 
 * @author jediminer543
 *
 */
public class EventWorldClick implements IEventUI, IEventLocated {
	
	boolean consumed;
	
	/**
	 * World in which this click event occured
	 */
	World w;
	/**
	 * The translated location of this click
	 */
	Coord c;

	/**
	 * The window this click originated from
	 */
	long window;
	
	/**
	 * Instantate a World Click event
	 * 
	 * @param window GLFW window origin of window click
	 * @param w The world in which this click is placed
	 * @param c The world localised click location
	 */
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
	
	@Override
	public World getEventWorld() {
		return w;
	}
	
	@Override
	public Coord getEventCoord() {
		return c;
	}

}
