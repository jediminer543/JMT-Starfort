package org.jmt.starfort.world.item;

import org.jmt.starfort.world.component.IComponent;

/**
 * Defines the item as being usable
 * @author jediminer543
 *
 */
public interface IItemUsable {

	/**
	 * Use the item on nothing in perticular.
	 * 
	 * Can be used for turning on/off, etc.
	 * 
	 * @param target the target Component (or item as items \approx components)
	 * @return The result of the interaction
	 * 
	 * TODO: create result enum (I.e. Fail NYI vs Fail ItemState vs Fail Target, etc.)
	 */
	public boolean useItem(IComponent target);
	
}
