package org.jmt.starfort.world.entity;

import org.jmt.starfort.pathing.bruteforce.IPassageCallback;
import org.jmt.starfort.pathing.bruteforce.Path;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.controller.entity.ControllerEntityAI;
import org.jmt.starfort.world.entity.ai.AIUtil;
import org.jmt.starfort.world.entity.ai.AIUtil.MoveState;
import org.jmt.starfort.world.entity.ai.ITask;
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
				this.getEntityAITask().execute(w, c, ie);
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
	 * @return True one at destination
	 */
	public default boolean moveTo(World w, Coord cur, IEntity ie, Coord dest) {
		return AIUtil.controledEntityMoveTo(w, cur, ie, dest, getEntityAIMoveState());
	}
	
	public default boolean interactWithComponent(World w, Coord cur, IComponent comp) {
		return false;
	}
	
	public default  boolean useItem(World w, Coord cur, IItem item) {
		return false;
	}
	
	public default  boolean useItemOnComponent(World w, Coord cur, IComponent comp) {
		return false;
	}
	
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
	 * Gets the move state, which encapsulates it's path,
	 * it's future path, and it's wait states.
	 * 
	 * Also means that changes to pathfinding result in less code changes.
	 * 
	 * @return See above
	 */
	public MoveState getEntityAIMoveState();
	
}
