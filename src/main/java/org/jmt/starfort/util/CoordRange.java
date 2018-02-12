package org.jmt.starfort.util;

import java.util.Iterator;

import org.jmt.starfort.world.World;

/**
 * Used for itterating through a 3d region of space without multiple FOR loops
 * 
 * Because 3 level for loops are irritating.
 * 
 * @author jediminer543
 *
 */
public class CoordRange implements Iterable<Coord>, Iterator<Coord> {

	public Coord min, max;
	
	Coord cur;
		
	/**
	 * Initialises CoordRange between min(a, b) and max(a, b)
	 * 
	 * @param a One corner of the 3d range
	 * @param b Other corner of range
	 */
	public CoordRange(Coord a, Coord b) {
		min = a.min(b);
		max = a.max(b);
		cur = min.get();
		cur.x -= 1;
	}
	
	/**
	 * Initialises CoordRange between (minX, minY, minZ) and (maxX, maxY, maxZ)
	 * 
	 */
	public CoordRange(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this(new Coord(minX, minY, minZ), new Coord(maxX, maxY, maxZ));
	}
	
	/**
	 * Initialises CoordRange using world bounds
	 * 
	 * @see World#getBounds(boolean)
	 * 
	 */
	public CoordRange(int[] bounds) {
		this(new Coord(bounds[0], bounds[1], bounds[2]), new Coord(bounds[3], bounds[4], bounds[5]));
	}
	
	@Override
	public Iterator<Coord> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return !cur.equals(max);
	}

	@Override
	public Coord next() {
		cur.x += 1;
		if (cur.x > max.x) {
			cur.x = min.x;
			cur.y += 1;
		}
		if (cur.y > max.z) {
			cur.y = min.y;
			cur.z += 1;
		}
		return cur.get();
	}

}
