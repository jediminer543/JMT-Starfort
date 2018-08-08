package org.jmt.starfort.pathing.nodemap;

import java.util.ArrayList;

import org.jmt.starfort.util.Coord;

public class PathingNode {
	
	//Nodes confirmed to be linked
	ArrayList<PathingNode> linkedNodes = new ArrayList<>();
	
	Coord nodeLocation;
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof PathingNode))
			return false;
		return ((PathingNode) o).nodeLocation.equals(nodeLocation);
	}
	
	@Override
	public int hashCode() {
		return nodeLocation.hashCode();
	}
	
}