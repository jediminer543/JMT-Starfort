package org.jmt.starfort.world.component.designator;

public interface IComponentDesignatorWorked {
	
	public int getDesignatorWorkNeeded();
	
	public float getDesignatorWorkDone();
	
	public void designatorDoWork(float amount);

}
