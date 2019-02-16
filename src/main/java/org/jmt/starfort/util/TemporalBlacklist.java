package org.jmt.starfort.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Stores items that are to be blacklisted for a given amount of time
 * 
 * Operates on same cost as Hashmap complexity wise
 * 
 * Usefull for avoiding pathing issues (which is it's purpose
 * 
 * @author jediminer543
 *
 * @param <T> Element to look against; I.e. for pathing use IEntity
 */
public class TemporalBlacklist<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4725397073342078014L;
	Map<T, Long> backing = new HashMap<>();
	long storeTime = 10000;
	
	public TemporalBlacklist() {}
	
	public TemporalBlacklist(long storeTime) {
		this.storeTime = storeTime;
	}
	
	public void add(T thing) {
		backing.put(thing, System.currentTimeMillis());
	}
	
	public boolean contains(T thing) {
		Long time = backing.get(thing);
		if (time == null) return false;
		return time+storeTime>System.currentTimeMillis();
	}
}
