package org.jmt.starfort.world.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.util.Coord;
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

	//Cache lifetime in NANOS; Currently set to 1 second
	long cacheTimeout = 1000000000;
	
	/**
	 * Contains a list of all component classes to lookup
	 * 
	 * A list of keys; if one of these doesn't exist in the maps, it will be added
	 */
	private ArrayList<Class<? extends IComponent>> toLookup = new ArrayList<>();
	
	/**
	 * Cache of looked up classes
	 */
	private Map<Class<? extends IComponent>, ArrayList<Coord>> toLookupCache = new ConcurrentHashMap<Class<? extends IComponent>, ArrayList<Coord>>();
	
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
	
	@Override
	public ComplexRunnable getTick() {
		return (Object... args) -> {
			World w = (World) args[0];
			boolean complete = false;
			while (!complete) {
				if (stage == -1) {
					//check integrity
					//List of classes to purge due to timeout
					List<Class<? extends IComponent>> toPurge = new ArrayList<>();
					for (Class<? extends IComponent> c : toLookup) {
						if (toLookupLastAddTime.containsKey(c)) {
							if (toLookupLastAddTime.get(c) + cacheTimeout < System.nanoTime()) {
								if (!toLookupCache.containsKey(c)) {
									toLookupCache.put(c, new ArrayList<>());
								}
							} else {
								toPurge.add(c);
							}
						} else {
							toLookupLastAddTime.put(c, System.nanoTime());
							if (!toLookupCache.containsKey(c)) {
								toLookupCache.put(c, new ArrayList<>());
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

	public ArrayList<Coord> findAllComponents(Class<? extends IComponent> type) {
		toLookupLastAddTime.put(type, System.nanoTime());
		if (toLookup.contains(type)) {
			if (toLookupCache.containsKey(type)) {
				return toLookupCache.get(type);
			}
		} else {
			toLookup.add(type);
		}
		return null;
	}
	
}
