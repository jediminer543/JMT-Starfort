package org.jmt.starfort.world.material;

import java.io.Serializable;

public interface IMaterial extends Serializable {
	
	public String getMaterialName();
	
	public float getMaterialHardness();
	
	public IMaterialType getMaterialType();
}
