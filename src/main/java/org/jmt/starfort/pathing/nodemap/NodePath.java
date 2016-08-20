package org.jmt.starfort.pathing.nodemap;

import java.util.Stack;

public class NodePath {

	Stack<PathingNode> path = new Stack<>();
	
	public int remaining() {
		return path.size();
	}
	
	public void push(PathingNode d) {
		path.push(d);
	}
	
	public PathingNode pop() {
		return path.pop();
	}
	
	/**
	 * Used in path construction, as the path will be built
	 */
	public void flip() {
		Object[] bak = path.toArray();
		path.clear();
		for (int i = bak.length-1; i >= 0; i--) {
			path.push((PathingNode) bak[i]);
		}
	}
}
