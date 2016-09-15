package org.jmt.starfort.world.entity.ai;

public interface ITask {

	public String getTaskName();
	
	public int getTaskPriority();
	
	public void doTask();
}