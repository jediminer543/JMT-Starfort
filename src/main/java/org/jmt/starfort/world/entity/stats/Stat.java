package org.jmt.starfort.world.entity.stats;

public class Stat<T> implements Comparable<Stat<?>> {
	
	T val;
	String name;
	
	public Stat(String name, T val) {
		this.name = name;
		this.val = val;
	}
	
	public String getStatName() {
		return name;
	}
	
	public T getStatData() {
		return val;
	}

	@Override
	public int compareTo(Stat<?> o) {
		return o.getStatName().compareTo(getStatName());
	}

}
