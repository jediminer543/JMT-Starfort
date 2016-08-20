package org.jmt.starfort.util;

import java.util.UUID;

public class WorldCoord extends Coord {

	/**
	 * The world this coord is tied to;
	 */
	UUID worldID;
	
	/**
	 * Create a null world coord; x, y, z set to 0, worldID set to null;
	 */
	public WorldCoord() {
		super();
		worldID = null;
	}
	
	/**
	 * Creates a worldcoord with a value
	 * 
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 * @param worldID the world this coord is tied to
	 */
	public WorldCoord(int x, int y, int z, UUID worldID) {
		super(x, y, z);
		this.worldID = worldID;
	}
}
