package org.jmt.starfort.pathing.bruteforce;

import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.world.World;

public interface IPassageCallback {

	public boolean canPass(World w, Coord src, Direction dir);
}
