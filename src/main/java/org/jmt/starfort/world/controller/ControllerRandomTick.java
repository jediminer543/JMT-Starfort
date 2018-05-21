package org.jmt.starfort.world.controller;

import java.util.Map.Entry;

import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.Block;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponentRandomTickable;

/**
 * TODO
 * @author Jediminer543
 *
 */
public class ControllerRandomTick implements IController {

	@Override
	public ComplexRunnable getTick() {
		return new ComplexRunnable() {
			
			@Override
			public void run(Object... args) {
				World w = (World) args[0];
				for (Entry<Coord, Block> entry : w.getBlocks().entrySet()) {
					entry.getValue().getCompInstances(IComponentRandomTickable.class);
				}
			}
		};
	}

}
