package org.jmt.starfort.world.entity.organs;

import java.util.ArrayList;
import java.util.List;

import org.jmt.starfort.world.material.IMaterial;

/**
 * Standardised implementation of IOrgan, to avoid creation 
 * of anonymous types, which are hard to serialise.
 * 
 * @author jediminer543
 * @see IOrgan
 */
public class Organ implements IOrgan {

	ArrayList<IOrgan> children = new ArrayList<>();
	IMaterial material;
	IOrganType type;
	String name;
	
	
	public Organ(IMaterial material, IOrganType type) {
		super();
		this.material = material;
		this.type = type;
	}

	@Override
	public IMaterial getOrganMaterial() {
		return material;
	}

	@Override
	public List<IOrgan> getOrganChildren() {
		return this.children;
	}

	@Override
	public IOrganType getOrganType() {
		return type;
	}

	@Override
	public String getOrganName() {
		return name;
	}

}
