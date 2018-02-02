package org.jmt.starfort.game.entity;

import java.util.concurrent.RunnableFuture;

import org.jmt.starfort.pathing.bruteforce.IPassageCallback;
import org.jmt.starfort.pathing.bruteforce.Path;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.entity.EntityAICapabilities;
import org.jmt.starfort.world.entity.IEntity;
import org.jmt.starfort.world.entity.IEntityAI;
import org.jmt.starfort.world.item.Item;

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

	//Move specific jobs
	Path p;
	transient RunnableFuture<Path> futurePath;
	
	@Override
	public boolean moveTo(Coord cur, Coord dest) {
		
		return false;
	}

	@Override
	public boolean interactWithComponent(Coord cur, Coord dest, IComponent comp) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean useItem(Coord cur, Coord dest, Item item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean useItemOnComponent(Coord cur, Coord dest, IComponent comp) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IPassageCallback getEntityAIPassageCallback() {
		// TODO Auto-generated method stub
		return null;
	}

}
