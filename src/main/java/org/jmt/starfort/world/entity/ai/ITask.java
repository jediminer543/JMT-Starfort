package org.jmt.starfort.world.entity.ai;

import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.entity.IEntity;
import org.jmt.starfort.world.entity.IEntityAI;

public interface ITask {
	
	public void process(World w, Coord c, IEntityAI eai, IEntity e);
}