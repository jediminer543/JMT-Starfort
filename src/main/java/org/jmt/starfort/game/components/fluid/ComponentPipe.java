package org.jmt.starfort.game.components.fluid;

import org.jmt.starfort.util.Direction;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentDirectioned;
import org.jmt.starfort.world.material.IMaterial;

/**
 * Was going to be a pipe
 * 
 * But then conduits exist now
 * 
 * So this is useless
 * 
 * @author jediminer543
 *
 */
@Deprecated
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
