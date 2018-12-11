package org.jmt.starfort.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.jmt.starfort.event.EventBus;
import org.jmt.starfort.event.world.EventMove;
import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.controller.IController;

/**
 * A world in which blocks exist
 * 
 * @author Jediminer543
 *
 */
public class World implements Serializable {

	/**
	 * For Saving
	 */
	private static final long serialVersionUID = -8189700013481707282L;

	/**
	 * For referencing and comparing with copies of this world
	 */
	UUID id = UUID.randomUUID();
	
	/**
	 * The blocks this world is made up of
	 */
	ConcurrentHashMap<Coord, Block> blocks = new ConcurrentHashMap<>();
	
	/**
	 * The controllers that maintain this world
	 */
	ArrayList<IController> controllers = new ArrayList<>();
	
	/**
	 * Creates a new instance of the World object
	 */
	public World () {} 
	
	/**
	 * Bounds of world
	 * 
	 * xmin, ymin, zmin, xmax, ymax, zmax
	 */
	int[] bounds = new int[6];
	
	/**
	 * Tick request of the world; required to allow for per-world time scaling
	 */
	TickRequest tickrequest;
	
	/**
	 * Gets the worlds tick request to suspend it or change it's rate
	 * @return World tick rate request
	 */
	public TickRequest getTickRequest() {
		return tickrequest;
	}
	
	/**
	 * Gets the block at a specific coordinate; if none exists it creates a blank one
	 * 
	 * @param c The coord to search for a block 
	 * @return The block at that coord
	 */
	public Block getBlock(Coord c) {
			if (!blocks.containsKey(c)) {
				blocks.put(c.get(), new Block());
			}
			return blocks.get(c);
	}
	
	/**
	 * Gets the block at a specific coordinate
	 * 
	 * @param c The coord to search for a block 
	 * @return The block at that coord
	 */
	public Block getBlockNoAdd(Coord c) {
			// READ THE DOCS; get returns null if not contained. no need to duplicate function
			// YOU ARE A DERP, DON'T CLEANUP, TO REMINED YOU THAT I AM IDIOT
			//if (!blocks.containsKey(c)) {
			//	return null;
			//} 
			return blocks.get(c);
	}
	
	/**
	 * Get the location of a block in the world space
	 * Returns null if block cannot be located
	 * 
	 * @param b The block to locate
	 * @return The coordinate of the block passed, or null if not found
	 */
	public Coord getBlockLocation(Block b) {
			for (Entry<Coord, Block> e: blocks.entrySet()) {
				if (e.getValue() == b) {
					return e.getKey().get();
				}
			}
		return null;
	}
	
	/**
	 * Gets the worlds next tick to be performed TODO
	 * 
	 * @return An array of all the ticks for this world
	 */
	public Map<Coord, ArrayList<ComplexRunnable>> getTicks() {
		ConcurrentHashMap <Coord, ArrayList<ComplexRunnable>> ticks = new ConcurrentHashMap <Coord, ArrayList<ComplexRunnable>>();
			for (Entry<Coord, Block> b: blocks.entrySet()) {
				ticks.put(b.getKey().get(), b.getValue().getTicks());
			}
		synchronized (controllers) {
			ArrayList<ComplexRunnable> controllerTicks = new ArrayList<>();
			for (IController c: controllers) {
				controllerTicks.add(c.getTick());
			}
			if (ticks.containsKey(new Coord())) {
				ticks.get(new Coord()).addAll(controllerTicks);
			} else {
				ticks.put(new Coord(), controllerTicks);
			}
		}
		return ticks;
	}
	
	/**
	 * Sets the block at a specific coord
	 * @param c The coord to be changed
	 * @param b The block to change to
	 */
	public void setBlock(Coord c, Block b) {
		synchronized (blocks) {
			blocks.put(c, b);
		}
	}
	
	/**
	 * Gets the worlds block array.
	 * 
	 * YOU REALLY SHOULDN'T BE CALLING THIS, AS IT COULD BREAK CONCURRENCY
	 * 
	 * @return the worlds block array
	 */
	public synchronized Map<Coord, Block> getBlocks() {
		return blocks;
	}
	
	/**
	 * Gets the worlds controller array
	 * @return the worlds block controller
	 */
	public synchronized ArrayList<IController> getController() {
		return controllers;
	}
	
	/**
	 * Get the controller of the specified class, or create new one if it doesn't exist
	 * 
	 * @param controllerClass The class of controller to find/add
	 * @return The controller, or null if it cannot be instantated
	 */
	@SuppressWarnings("unchecked") // apparently class.isInstance isn't checking casting
	public synchronized <T extends IController> T getController(Class<T> controllerClass) {
		for (IController c : controllers) {
			if (controllerClass.isInstance(c)) {
				// apparently class.isInstance isn't checking casting
				return (T) c;
			}
		}
		IController i = null;
		try {
			i = controllerClass.newInstance();
			controllers.add(i);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return (T) i;
	}
	
	/**
	 * Move a component from a coordinate to a coordinate, updating the relevant blocks
	 * and firing an EventMove across the event bus whilst doing so. 
	 * <br> If either of the referenced blocks does not exist, then the move will not occur. 
	 * If you wish to move to an un-populated coord, you should call getBlock() on the coord first.
	 * 
	 * @see EventMove
	 * 
	 * @param ic Component that will be moved, if not in the source block then no event will occur
	 * @param source The coordinate of the origin block
	 * @param dest The coordinate of the destination block
	 * 
	 * @return If the move event completed successfully
	 */
	public boolean moveComponent(IComponent ic, Coord source, Coord dest) {
		Block sb = getBlockNoAdd(source);
		Block db = getBlockNoAdd(dest);
		if (sb != null && db != null && sb.getComponents().contains(ic)) {
			EventBus.fireEvent(new EventMove(this, ic, source, dest));
			sb.removeComponent(ic);
			db.addComponent(ic);
			return true;
		}
		return false;
	}
	
	/**
	 * Updates the bounding box of the world in world coordinates
	 * 
	 * Complexity is tied to number of blocks in world, so calling
	 * should be minimised on large worlds, if done every tick.
	 * 
	 * @see World#getBounds(boolean)
	 */
	public void updateBounds() {
		int xmin, xmax, ymin, ymax, zmin, zmax;
		xmax = ymax = zmax = Integer.MIN_VALUE;
		xmin = ymin = zmin = Integer.MAX_VALUE;
		for (Entry<Coord, Block> e : blocks.entrySet()) {
			if (e.getKey().x > xmax)
				xmax = e.getKey().x;
			else if (e.getKey().x < xmin)
				xmin = e.getKey().x;
			
			if (e.getKey().y > ymax)
				ymax = e.getKey().y;
			else if (e.getKey().y < ymin)
				ymin = e.getKey().y;
			
			if (e.getKey().z > zmax)
				zmax = e.getKey().z;
			else if (e.getKey().z < zmin)
				zmin = e.getKey().z;
		}
		bounds = new int[]{xmin, ymin, zmin, xmax, ymax, zmax};
	}
	
	/**
	 * Gets the bounds of the world, recalculating if required.
	 * If the bounds have not been updated then it will return
	 * a `new int[8]`, so you should call update first time any
	 * thing calls this function
	 * 
	 * @param update Weather to recalculate the bounds
	 * @return {xmin, ymin, zmin, xmax, ymax, zmax} 
	 */
	public int[] getBounds(boolean update) {
		if (update)
			updateBounds();
		return bounds;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o.getClass() != this.getClass()) {
			return false;
		} else {
			return (((World) o).id == this.id);
		}
	}
}
