package org.jmt.starfort.world.entity.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class StatSheet {
	
	ArrayList<Stat<?>> stats = new ArrayList<>();
	boolean sorted = false;
	
	public Stat<?> getStat(String statName) {
		return getStat(new Stat<Object>(statName, null));
	}
	
	public Stat<?> getStat(Stat<?> stat) {
		if (!sorted) {
			Collections.sort(stats);
			sorted = true;
		}
		Stat<?> out = stats.get(Collections.binarySearch(stats, stat));
		if (out.getStatName() == stat.getStatName())
			return out;
		else
			return null;
	}
	
	public void addStat(Stat<?> stat) {
		Stat<?> toMerge = null;
		if ((toMerge = getStat(stat)) != null) {
			toMerge
		}
	}
	
	public static StatSheet merge(StatSheet ss1, StatSheet ss2) {
		
	}

}