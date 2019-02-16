package org.jmt.starfort.event.world;

import org.jmt.starfort.util.Coord;

/**
 * 
 * Indicates an event that happens at a specific location within a world
 * 
 * @author jediminer543
 *
 */
public interface IEventLocated extends IEventWorld {

	/**
	 * Get the location in the world at which this event occured
	 * 
	 * @return Above
	 */
	public Coord getEventCoord();
}
