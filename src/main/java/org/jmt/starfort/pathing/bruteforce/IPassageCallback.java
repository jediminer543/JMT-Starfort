package org.jmt.starfort.pathing.bruteforce;

import java.io.Serializable;

import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.world.World;

@FunctionalInterface
public interface IPassageCallback extends Serializable {

	public boolean canPass(World w, Coord src, Direction dir);
}
