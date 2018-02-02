package org.jmt.starfort.game.entity;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jmt.starfort.pathing.bruteforce.BruteforcePather;
import org.jmt.starfort.pathing.bruteforce.IPassageCallback;
import org.jmt.starfort.pathing.bruteforce.Path;
import org.jmt.starfort.processor.Processor;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;
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

	// Current location
	World w;
	Coord c;
	
	@Override
	public void setPosition(World w, Coord c) {
		this.w = w;
		this.c = c;
	}
	
	//Move specific vars
	Path p;
	transient RunnableFuture<Path> futurePath;
	
	@Override
	public boolean moveTo(Coord dest) {
		//Stateless statemachine
		//INGENIOUS
		
		//State 1; At dest
		if (c.equals(dest)) {
			return true;
		}
		//State 2: Need Path Generator
		if (p == null && futurePath == null) {
			futurePath = BruteforcePather.pathBetweenAsync(c, dest, w, getEntityAIPassageCallback());
			Processor.addRequest(futurePath);
		}
		//State 3: Path Generated
		if (futurePath != null && futurePath.isDone()) {
			try {
				p = futurePath.get(100, TimeUnit.MICROSECONDS);
				futurePath = null;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
			}
		}
		//State 4: Path exists
		if (p != null && p.remaining() <= 0 && futurePath == null) {
			futurePath = BruteforcePather.pathBetweenAsync(c, dest, w, getEntityAIPassageCallback());
			Processor.addRequest(futurePath);
			p = null;
		} else if (p != null && p.remaining() > 0) {
			Coord dst = c.addR(p.pop().getDir());
			w.moveComponent(baseEntity, c, dest);
		}
		return false;
	}

	@Override
	public boolean interactWithComponent(Coord pos, IComponent comp) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean useItem(Coord pos, Item item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean useItemOnComponent(Coord pos, IComponent comp) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IPassageCallback getEntityAIPassageCallback() {
		// TODO Auto-generated method stub
		return null;
	}

}
