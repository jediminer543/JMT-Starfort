package org.jmt.starfort.world.component;

import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.entity.IEntity;

/**
 * TODO
 * 
 * Allows entities to interact with this component
 * 
 * @author jediminer543
 *
 */
public interface IComponentInteractable extends IComponent {

	public boolean doComponentInteract(World w, Coord c, IEntity ie, Coord ec);
}
