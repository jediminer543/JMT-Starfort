package org.jmt.starfort.world.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

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
	long cacheTimeout = 1000000000l;
	
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
	
	int countdown = 0;
	int countdownMax = 10;
	
	protected void tick(Object... args) {
		w = (World) args[0];
		if (stage == -1) {
			//check integrity
			//List of classes to purge due to timeout
			List<Class<? extends IComponent>> toPurge = new ArrayList<>();
			for (Class<? extends IComponent> c : toLookup) {
				if (toLookupLastAddTime.containsKey(c)) {
					if (toLookupLastAddTime.get(c) + cacheTimeout > System.currentTimeMillis()) {
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
			if (toLookup.stream().filter(((Predicate<Class<? extends IComponent>>)toLookupCache::containsKey).negate()).count() > 0) {
				stage = 0;
			}
			if (!(countdown >= countdownMax)) {
				countdown++;
			} else {
				stage = -1;
			}
		}
	}
	
	private ComplexRunnable tick = this::tick;
	
	@Override
	public ComplexRunnable getTick() {
		return tick;
	}

	public List<Coord> findAllComponents(Class<? extends IComponent> type) {
		toLookupLastAddTime.put(type, System.currentTimeMillis());
		if (toLookup.contains(type)) {
			if (toLookupCache.containsKey(type)) {
				return toLookupCache.get(type);
			}
		} else {
			toLookup.add(type);
			toLookupLastAddTime.put(type, System.currentTimeMillis());
		}
		return null;
	}
	
	public <T extends IComponent> Map<T, Coord> genComponentLookup(World w, Class<T> type) {
		List<Coord> targets = findAllComponents(type);
		Map<T, Coord> out = new ConcurrentHashMap<>();
		for (Coord c : targets) {
			List<T> things = w.getBlock(c).getCompInstances(type);
			for (T thing : things) {
				out.put(thing, c);
			}
		}
		return out;
	}
	
	public Coord findComponentInstance(IComponent instance) {
		List<Coord> possibleTargets = findAllComponents(instance.getClass());
		if (possibleTargets != null) {
			for (Coord c : possibleTargets) {
				List<? extends IComponent> instances = w.getBlockNoAdd(c).getCompInstances(instance.getClass());
				if (instances.contains(instance)) {
					return c;
				}
			}
		}
		return null;
	}
	
}
