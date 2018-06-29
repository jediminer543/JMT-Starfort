package org.jmt.starfort.world.item;

/**
 * Represents a type of item
 * 
 * TODO: ADD Volume WHEN Material Details are done (Density etc.) so item mass can be calculated
 * 
 * @author jediminer543
 *
 */
public interface IItemType {

	/**
	 * Gets the unlocalised item type value
	 * 
	 * @return The unlocalized string
	 */
	public String getItemTypeName();

}
