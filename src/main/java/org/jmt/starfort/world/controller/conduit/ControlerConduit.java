package org.jmt.starfort.world.controller.conduit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jmt.starfort.logging.Logger;
import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.conduits.ConduitChannel;
import org.jmt.starfort.world.component.conduits.IComponentConduit;
import org.jmt.starfort.world.component.conduits.IComponentConduitDevice;
import org.jmt.starfort.world.component.conduits.IConduitChannelType;
import org.jmt.starfort.world.controller.IController;

/**
 * Controls conduit networks
 * 
 * Probably needs a massive rewrite
 * 
 * @author jediminer543
 *
 */
public class ControlerConduit implements IController {

	/**
	 * Serialisation ID
	 */
	private static final long serialVersionUID = 8876526825142851777L;

	// Allows task to be spread over multiple ticks
	//long maxLen = 50000;
	int lastX, lastY, lastZ; 
	
	//Cycling
	int networkingState = 0;
	long renetworkEvery = 100;
	long renetworkCount = 0;
	
	Map<Coord, IComponentConduit> conduits = new HashMap<Coord, IComponentConduit>();
	Map<Coord, IComponentConduitDevice> devices = new HashMap<Coord, IComponentConduitDevice>();
	
	List<List<Coord>> networks = new ArrayList<>();
	
	Direction[] procableDirs = new Direction[] {Direction.XDEC, Direction.ZDEC, Direction.YDEC, Direction.XINC, Direction.YINC, Direction.ZINC};
	
	public void tick(Object... args) {
		World w = (World)args[0];
		
		
		if (networkingState == 0) {
			//Reset Looping
			renetworkCount = renetworkEvery;
			
			//Find Conduits
			conduits = new HashMap<Coord, IComponentConduit>();
			devices = new HashMap<Coord, IComponentConduitDevice>();
			int[] bounds = w.getBounds(true);
			for (int x = bounds[0]; x < bounds[3]; x++) {
				for (int y = bounds[1]; y < bounds[4]; y++) {
					for (int z = bounds[2]; z < bounds[5]; z++) {
						if (w.getBlockNoAdd(new Coord(x, y, z)) != null) {
							if (w.getBlockNoAdd(new Coord(x, y, z)).getCompInstance(IComponentConduit.class) != null) {
								conduits.put(new Coord(x, y, z), w.getBlockNoAdd(new Coord(x, y, z)).getCompInstance(IComponentConduit.class));
							}
						}
					}
				}
			}
			networkingState = 1;
			return;
		} else if (networkingState == 1) {
			//Generate networks
			networks = new ArrayList<>();
			List<Coord> processed = new ArrayList<>();
			for (Entry<Coord, IComponentConduit> node : conduits.entrySet()) {
				if (!processed.contains(node.getKey())) {
					//Closed Set
					List<Coord> network = new ArrayList<>();
										
					//Open Set
					ArrayList<Coord> openSet = new ArrayList<>();
					openSet.add(node.getKey());
					
					List<Direction> toAdd = new ArrayList<>();
					
					while (openSet.size() > 0) {
						Coord current = openSet.remove(0);
						if (processed.contains(current)) {
							continue;
						}
						boolean configConduit = conduits.get(current).getConduitConnectedDirections() != null;
						toAdd.clear();
						for (Direction dir : procableDirs) {
							if (conduits.containsKey(current.add(dir.getDir())) && !network.contains(current.add(dir.getDir()))) {
								openSet.add(current.add(dir.getDir()));
							}
							if (devices.containsKey(current.add(dir.getDir()))) {
								network.add(current.add(dir.getDir()));
							}
							if (configConduit && ((conduits.containsKey(current.add(dir.getDir()))) || (devices.containsKey(current.add(dir.getDir()))))) {
								toAdd.add(dir);
							}
						}
						if (configConduit) synchronized (conduits.get(current).getConduitConnectedDirections()) {
							conduits.get(current).getConduitConnectedDirections().clear();
							conduits.get(current).getConduitConnectedDirections().addAll(toAdd);
							Logger.trace(toAdd.toString(), "ControllerConduit");
						}
						network.add(current);
						processed.add(current);
					}
					networks.add(network);
				}
			}
			networkingState = 2;
			return;
		} else if (networkingState == 2) {
			List<ConduitChannel> toAdd = new ArrayList<>();
			for (List<Coord> net : networks) {
				Map<IConduitChannelType, ConduitChannel> channels = new HashMap<>();
				for (Coord c : net) {
					if (devices.containsKey(c)) {
						toAdd.clear();
						for (IConduitChannelType chnt : devices.get(c).getConduitDeviceTypes()) { 
							if (!channels.containsKey(chnt)) {
								channels.put(chnt, new ConduitChannel(chnt));
							}
							toAdd.add(channels.get(chnt));
							channels.get(chnt).addDevice(devices.get(c));
						}
						synchronized (devices.get(c).getConduitDeviceChannels()) {
							devices.get(c).getConduitDeviceChannels().clear();
							devices.get(c).getConduitDeviceChannels().addAll(toAdd);
						}
					}
				}
			}
			networkingState = 3;
			return;
		} else if (networkingState == 3) {
			if (renetworkCount > 0) {
				renetworkCount--;
			} else {
				networkingState = 0;
			}
			return;
		}
	};
	
	@Override
	public ComplexRunnable getTick() {
		return this::tick;
	}

}
