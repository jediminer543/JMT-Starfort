package org.jmt.starfort.world.item;

import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.material.IMaterial;

/**
 * Used to designate a class that reprisents an item
 * 
 * Implements IComponent so it can be placed in world
 * 
 * @author Jediminer543
 *
 */
public class Item implements IComponent {

	IItemType itemType;
	IMaterial material;
	int count;
	
	public Item(IItemType itemType, IMaterial material) {
		this(itemType, material, 1);
	}
	
	public Item(IItemType itemType, IMaterial material, int count) {
		super();
		this.itemType = itemType;
		this.material = material;
		this.count = count;
	}

	/**
	 * Gets the localized name of the item.
	 * 
	 * Should generally be the localized name of the matetial(s) and the name of the item type
	 * 
	 * As per revision "Localisation is hard" localisation is delayed.
	 * 
	 * @return
	 */
	public String getItemName() {
		return this.getItemMaterial().getMaterialName() + " " + this.getItemType().getItemTypeName();
	}
	
	/**
	 * Gets the item type
	 * 
	 * @return
	 */
	public IItemType getItemType() {
		return itemType;
	}
	
	/**
	 * Gets the material the item is made of
	 * 
	 * @return
	 */
	public IMaterial getItemMaterial() {
		return material;
	}
	
	/**
	 * Return the number of items in this item.
	 * Confusing: Yes; removing the need for a designated item stack class: also yes.
	 * 
	 * @return Number of items in this Item group thing 
	 */
	public int getItemCount() {
		return count;
	}

	@Override
	public String getComponentName() {
		return this.getItemName();
	}

	@Override
	public IMaterial getComponentMaterial() {
		return this.getItemMaterial();
	}
}
