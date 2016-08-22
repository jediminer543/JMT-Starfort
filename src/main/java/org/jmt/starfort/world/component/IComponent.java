package org.jmt.starfort.world.component;

import org.jmt.starfort.world.material.IMaterial;

/**
 * These make up blocks
 * 
 * By using this system, it allows for multiple things to coexist in one space
 * which can be used to create cool stuff, such as wire bundles, or multiblock
 * devices that have many parts in one space (E.g. Nuclear reactor)
 * 
 * @author Jediminer543
 *
 */
public interface IComponent {
	
	/**
	 * Display name of the component
	 * 
	 * This should be an unlocalised string that is then looked up
	 * 
	 * @return name of the component
	 */
	public String getComponentName();
	
	/**
	 * Get's the materials primary material.
	 * 
	 * Will be added to name lookup
	 */
	public IMaterial getComponentMaterial();
	
	/**
	 * Get the component data in a string format <br>
	 * Format in JSON for arrays and other items
	 * <br>
	 * Common Tags:<br>
	 * 	name: the technical name of the component<br>
	 *  material: the material of the component<br>
	 *  
	 *  @return Map of the component
	 */
//	public Map<String, String> getComponentData();
}
