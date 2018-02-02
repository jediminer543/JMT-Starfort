package org.jmt.starfort.event.world;

import org.jmt.starfort.event.IEvent;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.TickRequest;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentTickable;

/**
 * An event indicating the movement of a component in the world
 * from one point to another. Useful for caching or Ticking.
 * 
 * Should ALWAYS be called when an instance of IComponentTickable
 * is moved
 * 
 * @author jediminer543
 * @see IComponentTickable
 * @see TickRequest
 */
public class EventMove implements IEvent {

	@Override
	public boolean getEventConsumed() {
		//Move event's aren't consumed
		return false;
	}

	@Override
	public void consumeEvent() {
		//Move event's aren't consumed
	}
	
	/**
	 * The component that has been moved
	 */
	public IComponent icomp;
	/**
	 * The original position of the component
	 */
	public Coord src;
	/**
	 * The final position of the component
	 */
	public Coord dst;
	/**
	 * The world in which this move takes place
	 * TODO add inter-world move, but that requires inter-world to begin with
	 */
	public World w;
	
	public EventMove(World w, IComponent icomp, Coord src, Coord dst) {
		this.icomp = icomp;
		this.src = src;
		this.dst = dst;
		this.w = w;
	}

}
