package org.jmt.starfort.world;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.block.Block;
//import org.jmt.starfort.world.controller.IController;

/**
 * A world in which blocks exist
 * 
 * @author Jediminer543
 *
 */
public class World {

	UUID id = UUID.randomUUID();
	
	HashMap<Coord, Block> blocks = new HashMap<>();
	//ArrayList<IController> controllers = new ArrayList<>();
	
	/**
	 * Gets the block at a specific coordinate; if none exists it creates a blank one
	 * 
	 * @param c The coord to search for a block 
	 * @return The block at that coord
	 */
	public synchronized Block getBlock(Coord c) {
		if (!blocks.containsKey(c)) {
			blocks.put(c, new Block(this));
		}
		return blocks.get(c);
	}
	
	/**
	 * Gets the worlds next tick to be performed TODO
	 * 
	 * @return An array of all the ticks for this world
	 */
	//public synchronized ArrayList<Runnable> getTicks() {
	//	ArrayList<Runnable> ticks = new ArrayList<>();
	//	for (Entry<Coord, Block> b: blocks.entrySet()) {
	//		ticks.addAll(b.getValue().getTicks(this, b.getKey()));
	//	}
	//	//for (IController c: controllers) {
	//	//	ticks.add(c.getTick(this));
	//	//}
	//	return ticks;
	//}
	
	/**
	 * Sets the block at a specific coord
	 * @param c The coord to be changed
	 * @param b The block to change to
	 */
	public synchronized void setBlock(Coord c, Block b) {
		blocks.put(c, b);
	}
	
	/**
	 * Gets the worlds block array
	 * @return the worlds block array
	 */
	public synchronized Map<Coord, Block> getBlocks() {
		return blocks;
	}
	
	/**
	 * Get the controller of the specified class, or create new one if it doesn't exist
	 * 
	 * @param controllerClass The class of controller to find/add
	 * @return The controller, or null if it cannot be instantated
	 */
	/* TODO:
	public synchronized IController getController(Class<? extends IController> controllerClass) {
		for (IController c : controllers) {
			if (controllerClass.isInstance(c)) {
				return c;
			}
		}
		IController i = null;
		try {
			i = controllerClass.newInstance();
			controllers.add(i);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return i;
	}
	*/
	
//	/**
//	 * <b>NYI:</b>
//	 * <br>
//	 * Returns a clone of the world map, allowing for non syncronus opperations, such as rendering
//	 * to be performed
//	 * 
//	 * @return
//	 */
//	public synchronized Map<Coord, Block> getBlocksClone() {
//		Map<Coord, Block> clone = Cloner.standard().deepClone(blocks);
//		return clone;
//	}
//	
//	/**
//	 * <b>NYI:</b>
//	 * <br>
//	 * Returns a clone of the world map, allowing for non syncronus opperations, such as rendering
//	 * to be performed
//	 * 
//	 * @return
//	 */
//	public synchronized Collection<Block> getBlocksValuesClone() {
//		Collection<Block> clone = Cloner.standard().deepClone(blocks.values());
//		return clone;
//	}
	
	@Override
	public boolean equals(Object o) {
		if (o.getClass() != this.getClass()) {
			return false;
		} else {
			return (((World) o).id == this.id);
		}
	}
}
