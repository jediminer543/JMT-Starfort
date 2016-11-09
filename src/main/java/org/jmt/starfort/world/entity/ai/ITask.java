package org.jmt.starfort.world.entity.ai;

import org.jmt.starfort.world.entity.IEntity;

public interface ITask {

	/**
	 * The name of the task
	 * @return the name of the task
	 */
	public String getTaskName();
	
	/**
	 * The priority of the task; higher = higher priority
	 * @return The task priority
	 */
	public int getTaskPriority();
	
	/**
	 * Update the task state
	 * @param args Input values being passed; First will be supertask
	 * @return true when the task is running (not waiting or completed)
	 */
	public boolean tickTask(Object... args);
	
	/**
	 * Get the state of the task
	 * @return State of the task
	 */
	public TaskState getTaskState();
	
	/**
	 * Return the task's more indepth status for user.
	 * I.e. Waiting for [MATERIAL] or Requires more power.
	 * @return Task's status description
	 */
	public String getTaskStateString();
	
	/**
	 * Check if the task can be performed
	 * @return Whether the task can be performed
	 */
	public boolean canTaskPerform(IEntity entity);
	
	/**
	 * Defines possible task states
	 * 
	 * @author Jediminer543
	 *
	 */
	public static enum TaskState {
		PRE,
		RUNNING,
		WAITING,
		STUCK,
		ERROR,
		COMPLETE;
	}
}