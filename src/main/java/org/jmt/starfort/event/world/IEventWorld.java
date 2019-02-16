package org.jmt.starfort.event.world;

import org.jmt.starfort.event.IEvent;
import org.jmt.starfort.world.World;

/**
 * 
 * Event interface for events that occur in worlds
 * 
 * @author jediminer543
 *
 */
public interface IEventWorld extends IEvent {

	/**
	 * World in which this event occured
	 * 
	 * @return World event takes place in
	 */
	public World getEventWorld();
	
}
