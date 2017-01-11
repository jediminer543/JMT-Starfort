package org.jmt.starfort.pathing.bruteforce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.world.World;

//TODO kill
public class BruteforcePather {

	public static RunnableFuture<Path> pathBetweenAsync(Coord src, Coord dst, World NodeSet, IPassageCallback passController) {
		return new FutureTask<Path>(new BruteforcePathingCallable(src, dst, NodeSet, passController));
	}
	
	public static class BruteforcePathingCallable implements Callable<Path> {

		Coord src, dst;
		World NodeSet;
		IPassageCallback passController;
		
		public BruteforcePathingCallable(Coord src, Coord dst, 
				World NodeSet, IPassageCallback passController) {
			this.src = src;
			this.dst = dst;
			this.NodeSet = NodeSet;
			this.passController = passController;
		}
		
		@Override
		public Path call() throws Exception {
			Path out = pathBetween(src, dst, NodeSet, passController);
			//System.out.println("Path calculated");
			return out;
		}
		
	}
	
	public static Path pathBetween(Coord src, Coord dst, World NodeSet, IPassageCallback passController) {
		ArrayList<Coord> closedSet = new ArrayList<>();
		
		ArrayList<Coord> openSet = new ArrayList<>();
		openSet.add(src);
		
		HashMap<Coord, Coord> cameFrom = new HashMap<>();
		
		HashMap<Coord, Integer> gScore = new HashMap<>();
		gScore.put(src, 0);
		
		HashMap<Coord, Integer> fScore = new HashMap<>();
		fScore.put(src, heuristic_cost_estimate(src, dst));
		
		int loopCount = 0;
		
		while (openSet.size() > 0) {
			int lowestCost = Integer.MAX_VALUE;
			Coord current = null;
			for (Coord pn : openSet) {
				if (fScore.get(pn) < lowestCost) {
					lowestCost = fScore.get(pn);
					current = pn;
				}
			}
			
			if (current.equals(dst))
				try {
					return reconstruct_path(cameFrom, current, dst);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			
	        openSet.remove(current);
	        closedSet.add(current);
	        for (Direction d : new Direction[] {Direction.XDEC, Direction.ZDEC, Direction.YDEC, Direction.XINC, Direction.YINC, Direction.ZINC}) {
	        	if (!passController.canPass(NodeSet, current, d))
	        		continue;
	        	Coord neighbor = current.addR(d.getDir());
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
	        loopCount++;
	        if (loopCount > 10000) {
	        	break;
	        }
		}
	
		return null;
	}
	
	private static Path reconstruct_path(HashMap<Coord, Coord> cameFrom, Coord current, Coord dst) throws Exception {
		Path path = new Path();
		//path.push(current);
		Coord newCurr;
		while (cameFrom.containsKey(current)) {
			newCurr = cameFrom.get(current);
			Direction d = Direction.getValueOf(newCurr.subR(current)).inverse();
			if (d != null) 
				path.push(d);
			else
				throw new Exception("SOMETHING BORKED");
			current = newCurr;
		}
		//path.flip();
		return path;
	}

	private static int heuristic_cost_estimate(Coord src, Coord dst) {
		Coord delta = src.subR(dst).absRM();
		return (delta.x)*(delta.x) + (delta.y * 5)*(delta.y * 5) + (delta.z)*(delta.z); 
		
	}
}
