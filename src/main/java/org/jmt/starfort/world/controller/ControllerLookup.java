package org.jmt.starfort.world.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.CoordRange;
import org.jmt.starfort.world.Block;
import org.jmt.starfort.world.TickRequest;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponent;

/**
 * Finds stuff in world, such as rooms //TODO//
 * components etc. Should implement caching.
 * 
 * Eventually
 * 
 * @author Jediminer543
 *
 */
public class ControllerLookup implements IController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5536182694166430815L;

	//Cache lifetime in Milis; Currently set to 100 second
	long cacheTimeout = 100000000l;
	
	/**
	 * Contains a list of all component classes to lookup
	 * 
	 * A list of keys; if one of these doesn't exist in the maps, it will be added
	 */
	private List<Class<? extends IComponent>> toLookup = new CopyOnWriteArrayList<>();
	
	/**
	 * Cache of looked up classes
	 */
	private Map<Class<? extends IComponent>, List<Coord>> toLookupCache = new ConcurrentHashMap<Class<? extends IComponent>, List<Coord>>();
	
	/**
	 * List of last referenced times; used to allow purging of cache
	 */
	private Map<Class<? extends IComponent>, Long> toLookupLastAddTime = new ConcurrentHashMap<Class<? extends IComponent>, Long>();
	
	/**
	 * Internal state counter; -1 = clearout; 0 = search;
	 * here to allow pause to ensure tick rate
	 * TODO IMPLEMENT
	 */
	private int stage = -1;
	
	private World w;
	
	@Override
	public ComplexRunnable getTick() {
		return (Object... args) -> {
			w = (World) args[0];
			boolean complete = false;
			while (!complete) {
				if (stage == -1) {
					//check integrity
					//List of classes to purge due to timeout
					List<Class<? extends IComponent>> toPurge = new ArrayList<>();
					for (Class<? extends IComponent> c : toLookup) {
						if (toLookupLastAddTime.containsKey(c)) {
							if (toLookupLastAddTime.get(c) + cacheTimeout < System.currentTimeMillis()) {
								if (!toLookupCache.containsKey(c)) {
									toLookupCache.put(c, new CopyOnWriteArrayList<>());
								}
							} else {
								toPurge.add(c);
							}
						} else {
							toLookupLastAddTime.put(c, System.currentTimeMillis());
							if (!toLookupCache.containsKey(c)) {
								toLookupCache.put(c, new CopyOnWriteArrayList<>());
							}
						}
					}
					//Purge timed out classes
					for (Class<? extends IComponent> c : toPurge) {
						toLookup.remove(c);
						toLookupCache.remove(c);
						toLookupLastAddTime.remove(c);
					}
					stage++;
				} else if (stage == 0) {
					for (Block b : w.getBlocks().values()) {
						for (Class<? extends IComponent> c : toLookup) {
							if (b.getCompInstance(c) != null) { 
								Coord coord = w.getBlockLocation(b);
								if (toLookupCache.get(c) == null) {
									toLookupCache.put(c, new CopyOnWriteArrayList<>());
								}
								toLookupCache.get(c).add(coord);
							}
						}
					}
					stage++;
				} else {
					stage = -1;
					complete = true;
				}
			}
		};
	}

	public List<Coord> findAllComponents(Class<? extends IComponent> type) {
		toLookupLastAddTime.put(type, System.currentTimeMillis());
		if (toLookup.contains(type)) {
			if (toLookupCache.containsKey(type)) {
				return toLookupCache.get(type);
			}
		} else {
			toLookup.add(type);
		}
		return null;
	}
	
	public Coord findComponentInstance(IComponent instance) {
		List<Coord> possibleTargets = findAllComponents(instance.getClass());
		if (possibleTargets != null) {
			for (Coord c : possibleTargets) {
				ArrayList<? extends IComponent> instances = w.getBlockNoAdd(c).getCompInstances(instance.getClass());
				if (instances.contains(instance)) {
					return c;
				}
			}
		}
		return null;
	}
	
}
