package org.jmt.starfort.world.entity.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class StatSheet {
	
	ArrayList<Stat<?>> stats = new ArrayList<>();
	boolean sorted = false;
	
	public Stat<?> getStat(String statName) {
		if (!sorted) {
			Collections.sort(stats);
			sorted = true;
		}
		return stats.get(Collections.binarySearch(stats, new Stat<Object>(statName, null)));
	}
	

}
