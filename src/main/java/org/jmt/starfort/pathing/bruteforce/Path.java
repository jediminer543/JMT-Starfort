package org.jmt.starfort.pathing.bruteforce;

import java.util.Stack;


import org.jmt.starfort.util.Direction;

public class Path {
	Stack<Direction> path = new Stack<>();
	
	public int remaining() {
		return path.size();
	}
	
	public void push(Direction d) {
		path.push(d);
	}
	
	public Direction pop() {
		return path.pop();
	}
	
	/**
	 * Used in path construction, as the path will be built
	 */
	public void flip() {
		Object[] bak = path.toArray();
		path.clear();
		for (int i = bak.length-1; i >= 0; i--) {
			path.push((Direction) bak[i]);
		}
	}
}
