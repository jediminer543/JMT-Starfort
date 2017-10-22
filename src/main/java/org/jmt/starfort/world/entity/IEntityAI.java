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
	public boolean moveTo(Coord c);
	
	public boolean interactWithComponent(Coord c, IComponent comp);
	
	public boolean useItem(Coord c, Item item);
	
	public boolean useItemOnComponent(Coord c, Item item, IComponent comp);
	
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
