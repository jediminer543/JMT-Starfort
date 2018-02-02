package org.jmt.starfort.world.entity;

import org.jmt.starfort.pathing.bruteforce.IPassageCallback;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;
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
	
	//PER TICK
	
	/**
	 * Should be called every tick; provides data necesary for AI functionality.
	 * 
	 * If you don't call this, your task will NOT WORK, and you shall be being
	 * judged by past me on that day (If it takes you multiple attempts to fix)
	 * 
	 * @param w The world the entity exists in
	 * @param c The current location of the entity in the world
	 */
	public void setPosition(World w, Coord c);
	
	//TASK METHODS
	public boolean moveTo(Coord dest);
	
	public boolean interactWithComponent(Coord pos, IComponent comp);
	
	public boolean useItem(Coord pos, Item item);
	
	public boolean useItemOnComponent(Coord pos, IComponent comp);
	
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
