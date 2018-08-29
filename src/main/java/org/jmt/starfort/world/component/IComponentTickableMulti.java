package org.jmt.starfort.world.component;

import java.util.List;

import org.jmt.starfort.processor.ComplexRunnable;

/**
 * For any component that exposes multiple complex runables as ticks; E.g. a component made of 
 * components (e.g. item inventories, etc.).
 * 
 * There is nothing stopping you making a component both a tickable and a multi tickable (i.e. main tick
 * vs inhereted ticks)
 * 
 * @author jediminer543
 *
 */
public interface IComponentTickableMulti extends IComponent {
	
	/**
	 * This should return the set of tick functions passed to the processing thread pool.
	 * <br><br>
	 * The values passed are World, Coord
	 * 
	 * @return An operation to pass to the processing pool
	 */
	public List<ComplexRunnable> getTicks();
}
