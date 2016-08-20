package org.jmt.starfort.world.material;

public interface IMaterial {
	
	public String getMaterialName();
	
	public float getMaterialHardness();
	
	public IMaterialType getMaterialType();
}
