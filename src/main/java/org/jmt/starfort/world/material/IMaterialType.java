package org.jmt.starfort.world.material;

/**
 * The type of a material, such as metals, or rocks, or plasmas
 * 
 * Used for grouping, and selecting default values for UI
 * I.e. sounds, etc.
 * At least in theory, if future me remembers.
 * 
 * @author jediminer543
 *
 */
public interface IMaterialType {

	public String getMaterialTypeName();
	
	public String getMaterialTypeDescriptor();
}
