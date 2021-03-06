package org.jmt.starfort.world.entity.ai;

import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.TickRequest;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.entity.IEntity;
import org.jmt.starfort.world.entity.IEntityAI;

/**
 * The sequence of instructions the AI must perform to
 * do a task. Will use the various methods in IEntityAI
 * 
 * @see IEntityAI
 * 
 * @author jediminer543
 *
 */
public interface ITask {
	
	/**
	 * Step the current task, calling functions from IEntityAI
	 * 
	 * @see IEntityAI
	 * 
	 * @param w World taken from {@link TickRequest}
	 * @param cur Coordinate taken {@link TickRequest}
	 * @param ai {@link IEntityAI} taken from current {@link IEntity}
	 */
	public void execute(World w, Coord cur, IEntity ie);
	
	/**
	 * Get's the state of this task
	 * 
	 * @return task's state
	 */
	public TaskState getTaskState();
}
