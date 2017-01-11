package org.jmt.starfort.world.entity.stats;

import java.util.ArrayList;
import java.util.Collections;

public class StatSheet {
	
	ArrayList<Stat<?>> stats = new ArrayList<>();
	boolean sorted = false;
	
	public Stat<?> getStat(String statName) {
		return getStat(new Stat<Object>(statName, null, Stat.StatType.Value));
	}
	
	public Stat<?> getStat(Stat<?> stat) {
		Stat<?> out = stats.get(Collections.binarySearch(stats, stat));
		if (out.getStatName() == stat.getStatName())
			return out;
		else
			return null;
	}
	
	@SuppressWarnings("unused") // because TODO
	public void addStat(Stat<?> stat) {
		Stat<?> toMerge = null;
		if ((toMerge = getStat(stat)) != null) {
			sorted = false;
			
		} else {
			sorted = false;
			
		}
	}
	
	public void checkSorted() {
		if (!sorted) {
			Collections.sort(stats);
			sorted = true;
		}
	}
	
	public static StatSheet merge(StatSheet ss1, StatSheet ss2) {
		return null;
		//TODO
	}

}