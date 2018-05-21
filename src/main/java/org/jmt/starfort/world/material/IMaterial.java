package org.jmt.starfort.world.material;

import java.io.Serializable;

/**
 * A material that something is made out of
 * 
 * This is the barest of minimal data for a material
 * 
 * More will likely be required later
 * 
 * @author jediminer543
 *
 */
public interface IMaterial extends Serializable {
	
	public String getMaterialName();
	
	public float getMaterialHardness();
	
	public IMaterialType getMaterialType();
}
