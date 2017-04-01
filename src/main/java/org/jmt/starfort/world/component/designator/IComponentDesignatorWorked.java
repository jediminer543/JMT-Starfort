package org.jmt.starfort.world.component.designator;

public interface IComponentDesignatorWorked extends IComponentDesignator {
	
	public int getDesignatorWorkNeeded();
	
	public float getDesignatorWorkDone();
	
	public void designatorDoWork(float amount);

}
