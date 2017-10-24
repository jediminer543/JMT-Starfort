package org.jmt.starfort.game.components.conduit;

import java.util.ArrayList;
import java.util.List;

import org.jmt.starfort.util.Direction;
import org.jmt.starfort.world.component.IComponentDirectioned;
import org.jmt.starfort.world.component.conduits.IComponentConduit;
import org.jmt.starfort.world.material.IMaterial;

public class ComponentConduit implements IComponentConduit, IComponentDirectioned {

	List<Direction> connected = new ArrayList<Direction>();
	
	@Override
	public String getComponentName() {
		return null;
	}

	@Override
	public IMaterial getComponentMaterial() {
		return null;
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
