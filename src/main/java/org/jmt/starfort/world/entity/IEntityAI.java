package org.jmt.starfort.world.entity;

import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.component.IComponent;

/**
 * Represents the mental capacities of the Entity
 * 
 * @author jediminer543
 */
public interface IEntityAI {
	
	public boolean moveTo(Coord c);
	
	public boolean interactWithComponent(Coord c, IComponent comp);
	
	//TODO add when Items are done. public boolean useItem(Coord c, IComponent comp);
	
	
}
