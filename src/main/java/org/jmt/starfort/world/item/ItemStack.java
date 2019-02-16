package org.jmt.starfort.world.item;

import org.jmt.starfort.world.material.IMaterial;

public class ItemStack implements IItem {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1145387976893097436L;

	private IItemStackable type = null;;
	private int itemCount = 0;
	
	public ItemStack(IItemStackable iis, int count) {
		this.type = iis;
		this.itemCount = count;
	}
	
	@Override
	public String getItemName() {
		return itemCount + " stack of " + type.getItemName();
	}

	@Override
	public IMaterial getItemMaterial() {
		return type.getItemMaterial();
	}

	@Override
	public int getItemValue() {
		return type.getItemValue()*itemCount;
	}
	
	public Class<? extends IItemStackable> getType() {
		return type.getClass();
	}

}
