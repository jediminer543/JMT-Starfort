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
import org.jmt.starfort.world.component.conduits.ConduitChannel;
import org.jmt.starfort.world.component.conduits.IComponentConduit;
import org.jmt.starfort.world.component.conduits.IComponentConduitDevice;
import org.jmt.starfort.world.component.conduits.IConduitChannelType;
import org.jmt.starfort.world.controller.IController;

public class ControlerConduit implements IController {

	// Allows task to be spread over multiple ticks
	//long maxLen = 50000;
	int lastX, lastY, lastZ; 
	
	//Cycling
	int networkingState = 0;
	long renetworkEvery = 500;
	long renetworkCount = 0;
	
	Map<Coord, IComponentConduit> conduits = new HashMap<Coord, IComponentConduit>();
	Map<Coord, IComponentConduitDevice> devices = new HashMap<Coord, IComponentConduitDevice>();
	
	List<List<Coord>> networks = new ArrayList<>();
	
	ComplexRunnable tick = (Object... args) -> {
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
		}
		else if (networkingState == 1) {
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
					
					while (openSet.size() > 0) {
						Coord current = openSet.remove(0);
						boolean configConduit = conduits.get(current).getConduitConnectedDirections() != null;
						if (configConduit) {
							conduits.get(current).getConduitConnectedDirections().clear();
						}
						for (Direction dir : new Direction[] {Direction.XDEC, Direction.ZDEC, Direction.YDEC, Direction.XINC, Direction.YINC, Direction.ZINC}) {
							if (conduits.containsKey(current.addR(dir.getDir()))) {
								openSet.add(current.addR(dir.getDir()));
							}
							if (devices.containsKey(current.addR(dir.getDir()))) {
								network.add(current.addR(dir.getDir()));
							}
							if (configConduit && ((conduits.containsKey(current.addR(dir.getDir()))) || (devices.containsKey(current.addR(dir.getDir()))))) {
								conduits.get(current).getConduitConnectedDirections().add(dir);
							}
						}
						network.add(current);
					}
					networks.add(network);
				}
			}
			networkingState = 2;
		} else if (networkingState == 2) {
			for (List<Coord> net : networks) {
				Map<IConduitChannelType, ConduitChannel> channels = new HashMap<>();
				for (Coord c : net) {
					if (devices.containsKey(c)) {
						devices.get(c).getConduitDeviceChannels().clear();
						for (IConduitChannelType chnt : devices.get(c).getConduitDeviceTypes()) { 
							if (!channels.containsKey(chnt)) {
								channels.put(chnt, new ConduitChannel(chnt));
							}
							devices.get(c).getConduitDeviceChannels().add(channels.get(chnt));
							channels.get(chnt).addDevice(devices.get(c));
						}
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
