package org.jmt.starfort.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.Set;

import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.util.NavContext;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentBlocking;
import org.jmt.starfort.world.component.IComponentTickable;
import org.jmt.starfort.world.component.IComponentTickableMulti;

public class Block implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6120827590177127003L;

	UUID id = UUID.randomUUID();
	
	volatile ArrayList<IComponent> components = new ArrayList<>();
	float damageTaken;
	
	public synchronized ArrayList<ComplexRunnable> getTicks() {
		ArrayList<ComplexRunnable> ticks = new ArrayList<>();
		for (IComponent c: components) {
			if (c instanceof IComponentTickable) {
				IComponentTickable tickcomp = (IComponentTickable) c;
				ComplexRunnable tick = tickcomp.getTick();
				if (tick != null)
					ticks.add(tick);
			}
			if (c instanceof IComponentTickableMulti) {
				IComponentTickableMulti tickcomp = (IComponentTickableMulti) c;
				List<ComplexRunnable> tickset = tickcomp.getTicks();
				if (tickset != null)
					ticks.addAll(tickset);
			}
		}
		return ticks;
	}
	
	public synchronized int addComponent(IComponent c) {
		components.add(c);
		return components.indexOf(c);
	}
	
	public synchronized void removeComponent(IComponent c) {
		//components.remove(c);
		components.remove(components.indexOf(c));
	}

	public synchronized IComponent getComponent(int pos) {
		return components.get(pos);
	}
	
	public synchronized ArrayList<IComponent> getComponents() {
		return components;
	}
	
	/**
	 * Get the directions of travel blocked by this block for specific navig contexts
	 * 
	 * @param context The nav context to search for
	 * @return Array of blocked directions
	 */
	public synchronized ArrayList<Direction> getBlockedDirs(NavContext context) {
		Set<Direction> blocked = new HashSet<>();
		for (IComponent c:components) {
			if (c instanceof IComponentBlocking) {
				IComponentBlocking comp = (IComponentBlocking) c;
				for (Entry<NavContext, Direction[]> b: comp.getComponentBlockedDirs().entrySet()) {
					if (b.getKey() == context) {
						blocked.addAll(Arrays.asList(b.getValue()));
					}
				}
			}
		}
		return new ArrayList<Direction>(blocked);
	}
	
	/**
	 * Get the first component implementing the defined class
	 * @param clazz the class the component must implement
	 * @return the first component found implementing the class, or null if none exists
	 */
	@SuppressWarnings("unchecked") // clazz.isInstance != checking
	public synchronized <T extends IComponent> T getCompInstance(Class<T> clazz) {
		for (IComponent c : components) {
			if (clazz.isInstance(c)) {
				return (T) c;
			}
		}
		return null;
	}
	
	/**
	 * Get all components implementing the defined class
	 * @param clazz the class the component must implement
	 * @return an array list of found components
	 */
	@SuppressWarnings("unchecked") // clazz.isInstance != checking
	public synchronized <T extends IComponent> ArrayList<T> getCompInstances(Class<T> clazz) {
		ArrayList<T> found = new ArrayList<T>();
		for (IComponent c : components) {
			if (clazz.isInstance(c)) {
				found.add((T) c);
			}
		}
		return found;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Block) && (((Block) obj).id == this.id);
	}
}
