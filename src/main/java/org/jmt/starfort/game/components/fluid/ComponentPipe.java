package org.jmt.starfort.game.components.fluid;

import org.jmt.starfort.util.Direction;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentDirectioned;
import org.jmt.starfort.world.material.IMaterial;

public class ComponentPipe implements IComponent, IComponentDirectioned {

	Direction[] connected;
	IMaterial mat;
	
	public ComponentPipe(Direction[] inlineArray, IMaterial mat) {
		connected = inlineArray;
		this.mat = mat;
	}

	@Override
	public String getComponentName() {
		return "Pipe";
	}

	@Override
	public IMaterial getComponentMaterial() {
		return mat;
	}

	@Override
	public Direction[] getComponentDirections() {
		return connected;
	}

}
