package org.jmt.starfort.world.entity.aiold;

import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;
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
	 * @param args Input values being passed;  supertask
	 * @return true when the task is running (not waiting or completed)
	 */
	public boolean tickTask(World w, IEntity e, Coord c, Object... args);
	
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
	 * Check if the task can be performed, will be called per tick; 
	 * in the event that an entity becomes incapable of performing it
	 * 
	 * I.e. pathfinding becomes blocked; entity SHOULD dispose of task and reaquire
	 * new task (May be the same task with different values; but it is recommended 
	 * to save processing by just resetting locally instead of reaquiring a new task);
	 * 
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