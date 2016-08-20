package org.jmt.starfort.world.entity.organs;

import java.util.ArrayList;

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
	
	public String getOrganName();
	
	
}
