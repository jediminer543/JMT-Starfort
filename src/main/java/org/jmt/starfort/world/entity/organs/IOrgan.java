package org.jmt.starfort.world.entity.organs;

import java.util.ArrayList;

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

	ArrayList<IOrgan> organs = new ArrayList<>();
	
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
}
