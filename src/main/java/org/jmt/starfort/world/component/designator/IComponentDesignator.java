package org.jmt.starfort.world.component.designator;

import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentInteractable;
import org.jmt.starfort.world.component.IComponentTasked;
import org.jmt.starfort.world.component.IComponentTickable;
import org.jmt.starfort.world.entity.IEntity;
import org.jmt.starfort.world.material.IMaterial;

/**
 * Defines a designator; a "task" in world
 * 
 * This may literally just sit there doing nothing
 * 
 * Todo: Implement
 * 
 * @author jediminer543
 *
 */
public interface IComponentDesignator extends IComponent, IComponentTasked, IComponentInteractable  {
	
	public boolean isDesignatorComplete();
	
	public int getDesignatorMaxWork();
	
	public int getDesignatorWork();
	
	public boolean doDesignatorWork(World w, Coord c, IEntity ie, Coord ec, int amount);
	
	@Override
	default boolean doComponentInteract(World w, Coord c, IEntity ie, Coord ec) {
		return doDesignatorWork(w, c, ie, ec, 1);
	}
}
