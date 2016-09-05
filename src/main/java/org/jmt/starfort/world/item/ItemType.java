package org.jmt.starfort.world.item;

public enum ItemType implements IItemType {

	//TODO localize
	Ingot("Ingot"),
	Chunk("");
	
	String typeName;
	
	ItemType(String typeName) {
		this.typeName = typeName;
	}
	
	@Override
	public String getItemTypeName() {
		return typeName;
	}

}
