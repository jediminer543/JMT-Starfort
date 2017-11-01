package org.jmt.starfort.world.entity.stats;

import java.util.ArrayList;
import java.util.List;

/**
 * A stat is a datapoint with 5 key stages:
 * <br><br>
 * <b>SET</b>: this stage determines the initial value of the stat;
 * 		the lowest will be chosen<br>
 * <b>PRE-ADD</b>: This defines pre-multiplication addition;
 * 		the sum of these values are added<br>
 * <b>MULT</b>: Multiplies the stat by the product of these values<br>
 * <b>POST-ADD</b>: Post multiplication addition values;
 * 		the sum of these is added<br>
 * <b>LIMIT</b>: Limits the value of this stat:
 * 		to the minimum of this value<br>
 * <br>
 * Stats initialise in such a way as to insure they will return a
 * value of zero when evaluated, and won't limit until one is set.
 * <br>
 * All components save Mult are integers, including return, as floats
 * have errors.
 * 
 * @author jediminer543
 *
 */
public class Stat {

	/**
	 * Initial Value of This Stat, Highest value will be used
	 */
	private List<Integer> set = new ArrayList<>();
	/**
	 * Additives to the stat, which WILL be multiplied
	 */
	private List<Integer> addPre = new ArrayList<>();
	/**
	 * Multipliers to the stat, which will be applied sequentially
	 */
	private List<Float> mult = new ArrayList<>();
	/**
	 * Additives to the stat, applied post multiplication
	 */
	private List<Integer> addPost = new ArrayList<>();
	/**
	 * Cap for the stat, the Minimum value will be used
	 */
	private List<Integer> limit = new ArrayList<>();
	
	/**
	 * Add default values, to stop errors.
	 */
	{
		set.add(0);
		mult.add(1f);
		limit.add(Integer.MAX_VALUE);
	}
	
	int resultant;
	
	boolean processed = false;
	
	public void clear() {
		// Clear all the array sets
		set.clear();
		addPre.clear();
		mult.clear();
		addPost.clear();
		limit.clear();
		
		//Restore default values
		set.add(0);
		mult.add(1f);
		limit.add(Integer.MAX_VALUE);
	}
	
	public void setPre(int i) {
		processed = false;
		set.add(i);
	}
	
	public void addPre(int i) {
		processed = false;
		addPre.add(i);
	}
	
	public void mult(float i) {
		processed = false;
		mult.add(i);
	}
	
	public void addPost(int i) {
		processed = false;
		addPost.add(i);
	}
	
	public void limit(int i) {
		processed = false;
		limit.add(i);
	}
	
	/**
	 * Resolves the stat to it's final value.
	 * 
	 * @return Final value of this stat.
	 */
	public int resolve() {
		if (!processed) {
			resultant = set.parallelStream().mapToInt(Integer::intValue).max().getAsInt();
			resultant += addPre.parallelStream().mapToInt(Integer::intValue).sum();
			resultant *= mult.parallelStream().mapToDouble(Float::doubleValue).reduce(1, (a, b) -> a * b);
			resultant += addPost.parallelStream().mapToInt(Integer::intValue).sum();
			resultant = Math.max(resultant, limit.parallelStream().mapToInt(Integer::intValue).min().getAsInt());
		}
		return resultant;
	}
}
