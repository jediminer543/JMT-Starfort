package org.jmt.starfort.game.entity;

import org.jmt.starfort.pathing.bruteforce.IPassageCallback;
import org.jmt.starfort.pathing.bruteforce.Path;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.entity.EntityAICapabilities;
import org.jmt.starfort.world.entity.IEntity;
import org.jmt.starfort.world.entity.IEntityAI;
import org.jmt.starfort.world.entity.ai.ITask;
import org.jmt.starfort.world.item.IItem;

/**
 * The standard implementation of EntityAI
 * @author jediminer543
 *
 */
public class EntityAI implements IEntityAI {
	
	IEntity baseEntity;
	EntityAICapabilities caps;
	
	public EntityAI(IEntity baseEntity, EntityAICapabilities caps) {
		this.baseEntity = baseEntity;
		this.caps = caps;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jmt.starfort.world.entity.IEntityAI#getEntityAICapibilities()
	 */
	@Override
	public EntityAICapabilities getEntityAICapibilities() {
		return caps;
	}
	
	@Override
	public IPassageCallback getEntityAIPassageCallback() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean moveTo(World w, Coord cur, IEntity ie, Coord dest) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public ITask getEntityAITask() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITask setEntityAITask(ITask task) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean interactWithComponent(World w, Coord cur, IComponent comp) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean useItem(World w, Coord cur, IItem item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean useItemOnComponent(World w, Coord cur, IComponent comp) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Path getEntityAIPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path setEntityAIPath(Path path) {
		// TODO Auto-generated method stub
		return null;
	}

}
