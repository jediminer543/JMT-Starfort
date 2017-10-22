package org.jmt.starfort.util;


/**
 * A representation of direction; Use for your own purposes
 * <br>
 * SELFFULL used for blocking
 * 
 * @author Jediminer543
 *
 * @see Coord
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
	 * <br><br>
	 * Usefull for addition to coordinates, for things like
	 * positioning.
	 * 
	 * @return Vector Coord of direction
	 * 
	 * @see Coord
	 */
	public Coord getDir() {
		return c;
	}
	
	/**
	 * Returns the reverse direction of this direction
	 * @return reverse direction
	 * @see #inverseOf(Direction)
	 */
	public Direction inverse() {
		return inverseOf(this);
	}
	
	/**
	 * Get the opposing direction to the passed direction
	 * 
	 * @param b Direction to find opposite of 
	 * @return Opposite direction to B
	 */
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
	
	/**
	 * Converts X changes to Z changes and Vice Versa
	 * 
	 * I honestly have no idea of the purpose of this function,
	 * just what it does.
	 * 
	 * @param d
	 * @return
	 */
	public static Direction xzFlip(Direction d) {
		if (d == Direction.SELFFULL || d == Direction.YINC || d == Direction.YDEC)
			return null;
		else if ((d.ordinal() & 2) == 0) {
			return Direction.values()[d.ordinal() + 2];
		} else {
			return Direction.values()[d.ordinal() - 2];
		}
	}
	
	/**
	 * Converts a Coord to a Direction. Assumes Coord is in same
	 * form as one given in direction definition.
	 * 
	 * @param c Coord, which should be a value of 1 or -1 in ONE of the dimentions, with the rest being 0, or 0 in all three
	 * @return the direction associated with that Coord, or null if none is found
	 * 
	 * @see #getDir()
	 * @see Coord
	 */
	public static Direction getValueOf(Coord c) {
		for (Direction d : values()) {
			if (d.getDir().equals(c)){
				return d;
			}
		}
		return null;
	}
}
