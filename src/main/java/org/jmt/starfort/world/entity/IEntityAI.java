package org.jmt.starfort.world.entity;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jmt.starfort.pathing.bruteforce.BruteforcePather;
import org.jmt.starfort.pathing.bruteforce.IPassageCallback;
import org.jmt.starfort.pathing.bruteforce.Path;
import org.jmt.starfort.processor.Processor;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.entity.ai.ControllerEntityAI;
import org.jmt.starfort.world.entity.ai.ITask;
import org.jmt.starfort.world.entity.ai.TaskState;
import org.jmt.starfort.world.item.IItem;

/**
 * Represents the mental capacities of the Entity
 * 
 * @author jediminer543
 */
public interface IEntityAI {

	//Capiablility
	
	public EntityAICapabilities getEntityAICapibilities();
	
	//PER TICK
	
	/**
	 * Advances EntityAI operations; performs the curently aquired task.
	 * 
	 * Default implementation finds the highest priority compatible task, and
	 * performs it.
	 * 
	 * @param w
	 * @param c
	 * @param ie
	 */
	public default void tickEntityAI(World w, Coord c, IEntity ie) {
		if (this.getEntityAITask() == null) {
			this.setEntityAITask(w.getController(ControllerEntityAI.class).getTask(ie));
		} else { 
			switch(this.getEntityAITask().getTaskState()) {
			case CONTINUE:
				this.getEntityAITask().execute(w, c, this);
			case WAIT:
				break;
			default:
				//DONE, ERROR or null
				this.setEntityAITask(null);
			}
		}
	}
	
	//TASK METHODS
	
	/**
	 * 
	 * @param w
	 * @param cur
	 * @param ie
	 * @param dest
	 * @return
	 */
	public boolean moveTo(World w, Coord cur, IEntity ie, Coord dest);
	
	public boolean interactWithComponent(World w, Coord cur, IComponent comp);
	
	public boolean useItem(World w, Coord cur, IItem item);
	
	public boolean useItemOnComponent(World w, Coord cur, IComponent comp);
	
	// STUFF METHODS
	/**
	 * Gets the passage callback for the entity
	 * 
	 * Required for navigating
	 * 
	 * @return An implementation of passage callback
	 */
	public IPassageCallback getEntityAIPassageCallback();
	
	public ITask getEntityAITask();
	
	public ITask setEntityAITask(ITask task);
	
	/**
	 * Get's the current AI path
	 * 
	 * @return See above
	 */
	public Path getEntityAIPath();
	
	/**
	 * Set's the current AI path
	 * 
	 * @return The previous path that was in use
	 */
	public Path setEntityAIPath(Path path);
}
