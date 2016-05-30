package org.jmt.starfort.util;


/**
 * A representation of direction; Use for your own purposes
 * <br>
 * SELFFULL used for blocking
 * 
 * @author Jediminer543
 *
 */
public enum Direction  {
	
	/**
	 * South
	 */
	XINC(new Coord(1, 0, 0)),
	
	/**
	 * North
	 */
	XDEC(new Coord(-1, 0, 0)),
	
	/**
	 * East
	 */
	ZINC(new Coord(0, 0, 1)),
	
	/**
	 * West
	 */
	ZDEC(new Coord(0, 0, -1)),
	
	/**
	 * Up
	 */
	YINC(new Coord(0, 1, 0)),
	
	/**
	 * Down
	 */
	YDEC(new Coord(0, -1, 0)),
	
	/**
	 * Nowhere
	 */
	SELFFULL(new Coord(0, 0, 0));
	
	Coord c;
	
	Direction(Coord c) {
		this.c = c;
	}
	
	/**
	 * Returns the vector value of the direction
	 * @return vector coord of direction
	 */
	public Coord getDir() {
		return c;
	}
	
	/**
	 * Returns the reverse direction of this direction
	 * @return reverse direction
	 */
	public Direction inverse() {
		return inverseOf(this);
	}
	
	public static Direction inverseOf(Direction b) {
		if (b == Direction.SELFFULL)
			return null;
		//apparently faster than x % 2 == 0
		else if ((b.ordinal() & 1) == 0) {
			return Direction.values()[b.ordinal() + 1];
		} else {
			return Direction.values()[b.ordinal() - 1];
		}
			
	}
	
	public static Direction xzFlip(Direction d) {
		if (d == Direction.SELFFULL || d == Direction.YINC || d == Direction.YDEC)
			return null;
		else if ((d.ordinal() & 2) == 0) {
			return Direction.values()[d.ordinal() + 2];
		} else {
			return Direction.values()[d.ordinal() - 2];
		}
	}
	
	public static Direction getValueOf(Coord c) {
		for (Direction d : values()) {
			if (d.getDir().equals(c)){
				return d;
			}
		}
		return null;
	}
}
