package org.jmt.starfort.world.entity.stats;

public class Stat<T> implements Comparable<Stat<?>> {
	
	T val;
	String name;
	StatType type;
	
	public Stat(String name, T val, StatType t) {
		this.name = name;
		this.val = val;
		this.type = t;
	}
	
	public String getStatName() {
		return name;
	}
	
	public T getStatData() {
		return val;
	}
	
	public StatType getStatType() {
		return type;
	}

	@Override
	public int compareTo(Stat<?> o) {
		return o.getStatName().compareTo(getStatName());
	}
	
	public static enum StatType {
		Value,
		PercentageMod,
		MultMod,
		SumMod;
	}

}
