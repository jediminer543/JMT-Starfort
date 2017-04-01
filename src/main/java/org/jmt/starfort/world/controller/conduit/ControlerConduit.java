package org.jmt.starfort.world.controller.conduit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.conduits.IComponentConduit;
import org.jmt.starfort.world.controller.IController;

public class ControlerConduit implements IController {

	// Allows task to be spread over multiple ticks
	long maxLen = 50000;
	int lastX, lastY, lastZ; 
	boolean network;
	
	Map<Coord, IComponentConduit> conduits = new HashMap<Coord, IComponentConduit>();
	
	ComplexRunnable tick = (Object... args) -> {
		World w = (World)args[0];
		
		if (network) {
			List<Coord> processed = new ArrayList<>();
			List<List<Coord>> networks = new ArrayList<>();
			for (Entry<Coord, IComponentConduit> node : conduits.entrySet()) {
				if (!processed.contains(node.getKey())) {
					//Closed Set
					List<Coord> network = new ArrayList<>();
					
					//Open Set
					ArrayList<Coord> openSet = new ArrayList<>();
					openSet.add(node.getKey());
					
					while (openSet.size() > 0) {
						for (Direction dir : new Direction[] {Direction.XDEC, Direction.ZDEC, Direction.YDEC, Direction.XINC, Direction.YINC, Direction.ZINC}) {
							
						}
					}
					
				}
				
			}
		} else {
			int[] bounds = w.getBounds(true);
			for (int x = bounds[0]; x < bounds[3]; x++) {
				for (int y = bounds[1]; y < bounds[4]; y++) {
					for (int z = bounds[2]; z < bounds[5]; z++) {
							if (w.getBlockNoAdd(new Coord(x, y, z)) != null)
								conduits.put(new Coord(x, y, z), w.getBlockNoAdd(new Coord(x, y, z)).getCompInstance(IComponentConduit.class));
					}
				}
			}
		}
	};
	
	@Override
	public ComplexRunnable getTick() {
		return tick;
	}

}
