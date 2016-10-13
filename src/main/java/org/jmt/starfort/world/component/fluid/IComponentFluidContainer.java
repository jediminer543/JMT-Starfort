package org.jmt.starfort.world.component.fluid;

import org.jmt.starfort.util.Direction;

public interface IComponentFluidContainer {
	
	/**
	 * Get the directions fluids can flow from this block
	 * @return The directions fluids can flow from this block
	 */
	public Direction[] getComponentFluidFlowable();
	
	public int getComponentFluidAmount();
	
	public int getComponentFluidMax();
	
	public int getComponentFluid();
	
	public void modComponentFluidAmount(int change);
	
}
