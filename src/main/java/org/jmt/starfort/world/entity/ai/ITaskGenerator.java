package org.jmt.starfort.world.entity.ai;

import org.jmt.starfort.world.entity.IEntityAI;

/**
 * Generates tasks for a given Entity Ai
 * @author jediminer543
 *
 */
public interface ITaskGenerator {

	/**
	 * Verify if any task is completable by the passed entity AI.
	 * Should check resources etc, but shouldn't bother with pathing
	 * checks (which are slow).
	 * 
	 * @param entity The entity AI to be checked
	 * @return Whether there is ANY completable task generatable
	 */
	public boolean isTaskGeneratorCompletable(IEntityAI entity);
	
	/**
	 * Count how many potentially completable tasks there are,
	 * used to cull empty/waiting task generators.
	 * 
	 * Return Integer.Max for Uncountable.
	 * 
	 * @return number of avaliable tasks
	 */
	public int avaliableTaskGeneratorTasks();
	
	/**
	 * Gets an appropriate task for the passed EntityAI
	 * 
	 * @param entity The entity AI that will be completing this task
	 * @return A VALID task for the entity to complete
	 */
	public ITask getTaskGeneratorTask(IEntityAI entity);
}
