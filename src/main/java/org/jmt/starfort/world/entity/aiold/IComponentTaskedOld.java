package org.jmt.starfort.world.entity.aiold;

import org.jmt.starfort.world.component.IComponent;

/**
 * A component that allocates tasks
 * 
 * @author Jediminer543
 *
 */
public interface IComponentTaskedOld extends IComponent {

	public ITask[] getComponentTasks();
}
