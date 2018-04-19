package org.jmt.starfort.pathing.nodemap;

import java.util.ArrayList;
import java.util.HashMap;

import org.jmt.starfort.util.Coord;

public class NodePather {

	//TODO ANYTHING AT ALL
	
	
	public NodePath pathBetween(PathingNode src, PathingNode dst, ArrayList<PathingNode> NodeSet) {
		ArrayList<PathingNode> closedSet = new ArrayList<>();
		
		ArrayList<PathingNode> openSet = new ArrayList<>();
		openSet.add(src);
		
		HashMap<PathingNode, PathingNode> cameFrom = new HashMap<>();
		
		HashMap<PathingNode, Integer> gScore = new HashMap<>();
		gScore.put(src, 0);
		
		HashMap<PathingNode, Integer> fScore = new HashMap<>();
		fScore.put(src, heuristic_cost_estimate(src, dst));
		
		while (openSet.size() > 0) {
			int lowestCost = Integer.MAX_VALUE;
			PathingNode current = null;
			for (PathingNode pn : openSet) {
				if (fScore.get(pn) < lowestCost) {
					lowestCost = fScore.get(pn);
					current = pn;
				}
			}
			
			if (current.equals(dst))
				return reconstruct_path(cameFrom, current);
			
	        openSet.remove(current);
	        closedSet.add(current);
	        for (PathingNode pn : NodeSet) {
	        	if (!pn.equals(current)) {
	        		if (pn.nodeLocation.x == current.nodeLocation.x && pn.nodeLocation.z == current.nodeLocation.z) {
	        			current.linkedNodes.add(pn);
	        		} else if (pn.nodeLocation.y == current.nodeLocation.y) {
	        			current.linkedNodes.add(pn);
	        		}
	        	}
	        }
	        for (PathingNode neighbor : current.linkedNodes) {
	        	if (closedSet.contains(neighbor))
	        		continue;
	        	
	        	int tentative_gScore = gScore.get(current) + heuristic_cost_estimate(current, neighbor);
	        	
	        	if (!openSet.contains(neighbor))
	        		openSet.add(neighbor);
	        	else if (tentative_gScore >= gScore.get(neighbor))
	        		continue;
	        	
	        	cameFrom.put(neighbor, current);
	        	
	        	gScore.put(neighbor, tentative_gScore);
	        	fScore.put(neighbor, tentative_gScore  + heuristic_cost_estimate(neighbor, dst));
	        	
	        }
		}
	
		return null;
	}
	
	private NodePath reconstruct_path(HashMap<PathingNode, PathingNode> cameFrom, PathingNode current) {
		NodePath path = new NodePath();
		path.push(current);
		while (cameFrom.containsKey(current)) {
			current = cameFrom.get(current);
			path.push(current);
		}
		path.flip();
		return path;
	}

	private int heuristic_cost_estimate(PathingNode src, PathingNode dst) {
		Coord delta = src.nodeLocation.sub(dst.nodeLocation).absM();
		return delta.x + delta.y + delta.z; 
		
	}
}
