package org.jmt.starfort.world.controller;

import org.jmt.starfort.processor.ComplexRunnable;

public interface IController {

	/**
	 * This is the tick function passed to the processing thread pool 
	 * <br><br>
	 * The values passed are World, null (the coord of the controller)
	 * 
	 * @return An operation to pass to the processing pool
	 */
	public ComplexRunnable getTick();
}
