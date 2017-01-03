package org.jmt.starfort.game.event.events;

import org.jmt.starfort.game.event.IEvent;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponent;

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
	
	public IComponent icomp;
	public Coord src;
	public Coord dst;
	public World w;
	
	public EventMove(World w, IComponent icomp, Coord src, Coord dst) {
		this.icomp = icomp;
		this.src = src;
		this.dst = dst;
		this.w = w;
	}

}
