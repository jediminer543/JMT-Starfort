package org.jmt.starfort.world.entity;

import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentTickable;
import org.jmt.starfort.world.entity.ai.ITask;
import org.jmt.starfort.world.entity.organs.IOrgan;

/**
 * An entity
 * 
 * 
 * 
 * @author Jediminer543
 *
 */
public interface IEntity extends IComponent, IComponentTickable {

	/**
	 * Returns the entities name (I.e. Giant Bird Man or Bob).
	 * 
	 * Use get component name for the entity's base, unlocalized name.
	 * <br><br>
	 * If 
	 * <pre> {@code getComponentName() == getEntityName()} </pre> then the entity has no sepcific name.
	 * 
	 * @see {@link org.jmt.starfort.world.component.IComponent.getComponentName} 
	 * 
	 * @return the entities name
	 */
	public String getEntityName();
	
	/**
	 * Returns the body's structured 
	 * 
	 * @return
	 */
	public IOrgan getEntityOrganBody();
	
	/**
	 * Gets the override tasklist for the entity,
	 * 
	 * should include stuff like drinking, eating,
	 * sleeping, etc.
	 * 
	 * @return Override task list
	 */
	public ITask[] getEntityTaskList();
	
}
