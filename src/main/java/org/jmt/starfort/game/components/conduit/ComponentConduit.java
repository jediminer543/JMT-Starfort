package org.jmt.starfort.game.components.conduit;

import java.util.ArrayList;
import java.util.List;

import org.jmt.starfort.util.Direction;
import org.jmt.starfort.world.component.IComponentDirectioned;
import org.jmt.starfort.world.component.conduits.IComponentConduit;
import org.jmt.starfort.world.material.IMaterial;

public class ComponentConduit implements IComponentConduit, IComponentDirectioned {

	public List<Direction> connected = new ArrayList<Direction>();
	IMaterial mat;
	
	public ComponentConduit(IMaterial mat) {
		this.mat = mat;
	}
	
	
	@Override
	public String getComponentName() {
		return "Conduit";
	}

	@Override
	public IMaterial getComponentMaterial() {
		return mat;
	}

	@Override
	public List<Direction> getConduitConnectedDirections() {
		return connected;
	}

	@Override
	public Direction[] getComponentDirections() {
		return (Direction[]) connected.toArray(new Direction[connected.size()]);
	}

}
