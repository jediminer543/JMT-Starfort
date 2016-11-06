package org.jmt.starfort.world.controller;

import org.jmt.starfort.processor.ComplexRunnable;

public class ControllerTask implements IController {

	@Override
	public ComplexRunnable getTick() {
		return new ComplexRunnable() {
			
			@Override
			public void run(Object... args) {
				
				
			}
		};
	}

}
