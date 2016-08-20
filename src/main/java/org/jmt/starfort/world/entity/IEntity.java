package org.jmt.starfort.world.entity;

import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentTickable;
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
	 * @return
	 */
	public IOrgan getOrganBody();
	
	/**
	 * The ability of the character to resist stuff, etc, and their body itself
	 * @return The entities body stat
	 */
	int getBody();
	
	/**
	 * The ability of the character to be strong
	 * @return The entities strength stat
	 */
	int getStrength();
	
	/**
	 * The ability of the character to coordinate, dodge, etc.
	 * @return The entities agility stat
	 */
	int getAgility();
	
	/**
	 * The ability of the character to react to their surroundings, their reflexses and their response times
	 * @return The entities reaction stat
	 */
	int getReaction();
	
	/**
	 * The ability of the character to resist will tests, magic and cast magic
	 * @return The entities willpower stat
	 */
	int getWillpower();
	
	/**
	 * The ability of the character to social
	 * @return The entities charisma stat
	 */
	int getCharisma();
	
	/**
	 * The ability of the character to solve a problem
	 * @return The entities logic stat
	 */
	int getLogic();
	
	/**
	 * The ability of the character to predict what may happen
	 * @return The entities intuition stat
	 */
	int getIntuition();
	
}
