package org.jmt.starfort.world.component.fluid;

import org.jmt.starfort.util.Direction;

public interface IComponentFluidContainer {
	
	public Direction[] getComponentFluidFlowable();
	
	public int getComponentFluidAmount();
	
	public int getComponentFluidMax();
	
	public int getComponentFluid();
	
}
