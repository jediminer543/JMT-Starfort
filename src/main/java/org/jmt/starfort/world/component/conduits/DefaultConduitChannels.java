package org.jmt.starfort.world.component.conduits;

public enum DefaultConduitChannels implements IConduitChannelType {
	
	Fluid("Fluid", "Moves fluids around"),
	Energy("Energy", "POWAH"),
	Data("Data", "Networks computers"),
	Command("Command", "Allows remote control");

	String name, desc;
	DefaultConduitChannels(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}
	
	@Override
	public String getConduitTypeName() {
		return name;
	}

	@Override
	public String getConduitTypeDescription() {
		return desc;
	}

}
