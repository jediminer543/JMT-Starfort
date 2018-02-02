package org.jmt.starfort.world.entity;

import org.jmt.starfort.pathing.bruteforce.IPassageCallback;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.item.Item;

/**
 * Represents the mental capacities of the Entity
 * 
 * @author jediminer543
 */
public interface IEntityAI {
	
	//Capiablility
	
	public EntityAICapabilities getEntityAICapibilities();
	
	//TASK METHODS
	public boolean moveTo(Coord cur, Coord dest);
	
	public boolean interactWithComponent(Coord cur, Coord dest, IComponent comp);
	
	public boolean useItem(Coord cur, Coord dest, Item item);
	
	public boolean useItemOnComponent(Coord cur, Coord dest, IComponent comp);
	
	// STUFF METHODS
	/**
	 * Gets the passage callback for the entity
	 * 
	 * Required for navigating
	 * 
	 * @return An implementation of passage callback
	 */
	public IPassageCallback getEntityAIPassageCallback();
	
}
