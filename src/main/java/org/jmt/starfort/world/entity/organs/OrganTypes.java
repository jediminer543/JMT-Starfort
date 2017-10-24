package org.jmt.starfort.world.entity.organs;

/**
 * Standard set of organs
 * @author jediminer543
 * @see IOrganType
 */
public enum OrganTypes implements IOrganType {
	Body("Body", "Parent organ of all organs"),
	Spine("Spine", "To hold you back"),
	Rib("Rib", "For being ribbed"),
	Arm("Arm", "For moving"),
	Hand("Hand", "For grabbing"),
	Leg("Leg", "For Standing"),
	Foot("Foot", "For Standing On"),
	Heart("Heart", "For Pumping"),
	Lungs("Lungs", "For Breathing"),
	Liver("Liver", "For Glucoregulation"),
	Stomach("Stomach", "For Digestion"),
	Brain("Brain", "For Thinking");
	
	String name, desc;
	OrganTypes(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}
	@Override
	public String getOrganTypeName() {
		return name;
	}
	@Override
	public String getOrganTypeDescription() {
		return desc;
	}
}
