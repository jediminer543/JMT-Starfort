package org.jmt.starfort.world.entity.organs;

import java.util.List;

import org.jmt.starfort.world.material.IMaterial;

/**
 * Defines an organ (part of an entity)
 * 
 * Used for both organic and non-organic entities
 * 
 * @author Jediminer543
 *
 */
public interface IOrgan {
	
	/**
	 * Get the name of the organ
	 * 
	 * Pls localise at some point
	 * 
	 * @return Organ name
	 */
	public String getOrganName();
	
	/**
	 * Get the material this organ is made of
	 * 
	 * Important for robots and cyberware (if cyber ever turns up)
	 * 
	 * @return Material of organ
	 */
	public IMaterial getOrganMaterial();
	
	/**
	 * Get the child organs of the current organs
	 * 
	 * @return Child organs of this organ
	 */
	public List<IOrgan> getOrganChildren();
	
	/**
	 * Gets the type of this organ
	 * 
	 * @return
	 */
	public IOrganType getOrganType();
}
