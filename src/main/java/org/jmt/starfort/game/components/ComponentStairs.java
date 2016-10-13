package org.jmt.starfort.game.components;

import org.jmt.starfort.util.Direction;
import org.jmt.starfort.util.InlineFunctions;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentDirectioned;
import org.jmt.starfort.world.component.IComponentUpDown;
import org.jmt.starfort.world.material.IMaterial;

public class ComponentStairs implements IComponent, IComponentDirectioned, IComponentUpDown {

	boolean up, down;
	IMaterial mat;
	
	public ComponentStairs(IMaterial mat, boolean up, boolean down) {
		this.up = up;
		this.down = down;
		this.mat = mat;
	}
	
	@Override
	public boolean canUp() {
		// TODO Auto-generated method stub
		return up;
	}

	@Override
	public boolean canDown() {
		// TODO Auto-generated method stub
		return down;
	}

	@Override
	public Direction[] getComponentDirections() {
		return up && down ? InlineFunctions.inlineArray(Direction.YINC, Direction.YDEC) : (down ? InlineFunctions.inlineArray(Direction.YDEC) : (up ? InlineFunctions.inlineArray(Direction.YINC) : InlineFunctions.inlineArray(Direction.SELFFULL)));
	}

	@Override
	public String getComponentName() {
		return getComponentMaterial().getMaterialName() +  "Stairs";
	}

	@Override
	public IMaterial getComponentMaterial() {
		// TODO Auto-generated method stub
		return mat;
	}

}
