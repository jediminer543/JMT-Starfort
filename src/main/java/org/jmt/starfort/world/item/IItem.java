package org.jmt.starfort.world.item;

import org.jmt.starfort.world.material.IMaterial;

/**
 * Used to designate a class that reprisents an item
 * 
 * @author Jediminer543
 *
 */
public interface IItem {

	/**
	 * Gets the localized name of the item.
	 * 
	 * Should generally be the localized name of the matetial(s) and the  
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * Gets the item type
	 * 
	 * @return
	 */
	public IItemType getItemType();
	
	/**
	 * Gets the material the item is made of
	 * 
	 * @return
	 */
	public IMaterial getItemMaterial();
	
	/**
	 * A value refering to the amount an item is worth for creation purposes, generaly where 100 is equal to one unit of stuff 
	 * (e.g. An ingot or plate of metal would be 100). Allows the fragmentation of items in construction, so an ingot may break down into
	 * say 3 chunks of 10 value each after 70 is used in crafting. Done with ints to avoid float math (especially since it is 
	 * doubtful that less than 0.01 of an item will be needed). 
	 * 
	 * @return value
	 */
	public int getItemTypeValue();
}
