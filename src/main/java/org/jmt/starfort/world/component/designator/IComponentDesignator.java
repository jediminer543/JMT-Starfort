package org.jmt.starfort.world.component.designator;

import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentTickable;

/**
 * Defines a designator; a task
 * 
 * @author Jediminer543
 *
 */
public interface IComponentDesignator extends IComponent, IComponentTickable {
	
	public void designatorTask(Object... args);
	
	public boolean isDesignatorComplete();
	
}
