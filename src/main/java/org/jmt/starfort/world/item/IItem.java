package org.jmt.starfort.world.item;

import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.material.IMaterial;

/**
 * An Item that can exist in the world
 * 
 * TODO: Add item stuffs
 * 
 * Currently just an extension of components
 * 
 * @author Jediminer543
 *
 */
public interface IItem extends IComponent {
	
	
	/**
	 * Get the items name; by default this is also the component name
	 * 
	 * @return The items name
	 */
	public String getItemName();
	
	default public String getComponentName() {
		return this.getItemName();
	}
	
	/**
	 * Get's the item's material; by default this is also the component material
	 * 
	 * @return The item's material
	 */
	public IMaterial getItemMaterial();
	
	default public IMaterial getComponentMaterial() {
		return this.getItemMaterial();
	}
	
	/**
	 * TODO: decide whether to use this or volume
	 * 
	 * The material value of the item; I.e. a log may be 1000 units of wood
	 * and a plank may be 250.
	 * 
	 * @return See Above
	 */
	public int getItemValue();
	
	
}
