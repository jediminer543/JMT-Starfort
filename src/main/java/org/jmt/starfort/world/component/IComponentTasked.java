package org.jmt.starfort.world.component;

import org.jmt.starfort.world.entity.aiold.ITask;

/**
 * A component that allocates tasks
 * 
 * @author Jediminer543
 *
 */
public interface IComponentTasked extends IComponent {

	public ITask[] getComponentTasks();
}
