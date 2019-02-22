package org.jmt.starfort.game.components;

import org.jmt.starfort.util.Direction;
import org.jmt.starfort.util.InlineFunctions;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentDirectioned;
import org.jmt.starfort.world.component.IComponentUpDown;
import org.jmt.starfort.world.material.IMaterial;

public class ComponentStairs implements IComponent, IComponentDirectioned, IComponentUpDown {

	private static final long serialVersionUID = -8764913098897973098L;
	/**
	 * Allowed directions of motion
	 */
	boolean up, down;
	/**
	 * Material of stairs
	 */
	IMaterial mat;
	
	protected ComponentStairs() {}
	
	public ComponentStairs(IMaterial mat, boolean up, boolean down) {
		this.up = up;
		this.down = down;
		this.mat = mat;
	}
	
	@Override
	public boolean canUp() {
		return up;
	}

	@Override
	public boolean canDown() {
		return down;
	}

	@Override
	public Direction[] getComponentDirections() {
		return up && down ? InlineFunctions.inlineArray(Direction.YINC, Direction.YDEC) : (down ? InlineFunctions.inlineArray(Direction.YDEC) : (up ? InlineFunctions.inlineArray(Direction.YINC) : InlineFunctions.inlineArray(Direction.SELFFULL)));
	}

	@Override
	public String getComponentName() {
		return "Stairs";
	}

	@Override
	public IMaterial getComponentMaterial() {
		return mat;
	}

}
