package org.jmt.starfort.world.component;

import org.jmt.starfort.processor.ComplexRunnable;

/**
 * Defines a component that needs ticking
 * 
 * @author Jediminer543
 *
 */
public interface IComponentTickable extends IComponent {

	/**
	 * This is the tick function passed to the processing thread pool
	 * <br><br>
	 * The values passed are World, Coord
	 * 
	 * @return An operation to pass to the processing pool
	 */
	public ComplexRunnable getTick();
}
