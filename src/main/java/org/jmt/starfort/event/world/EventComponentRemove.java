package org.jmt.starfort.event.world;

import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponent;

public class EventComponentRemove implements IEventLocated {

	Coord loc;
	World w;
	IComponent ic;

	@Override
	public World getEventWorld() {
		return w;
	}

	@Override
	public Coord getEventCoord() {
		return loc;
	}
	
	public IComponent getEventComponent() {
		return ic;
	}

	public EventComponentRemove(Coord loc, World w, IComponent ic) {
		super();
		this.loc = loc;
		this.w = w;
		this.ic = ic;
	}

}
