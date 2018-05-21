package org.jmt.starfort;

import java.util.ArrayList;

import org.jmt.starfort.game.components.ComponentWall;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.material.IMaterial;

/**
 * A class for creating interesting and usefull stuff for dev/test scenarios
 * 
 * Probably should be somewhere more usefull
 * 
 * But future me can deal with that
 * 
 * Update: Frack you, further future me can deal with it - Future me
 * 
 * @author jediminer543
 *
 */
public class DevUtil {

	public static void makeRoom(World w, IMaterial m, Coord minCorner, Coord maxCorner) {
		for (int x = minCorner.x; x <= maxCorner.x; x++) {
			boolean XDEC = x == minCorner.x;
			boolean XINC = x == maxCorner.x;
			for (int z = minCorner.z; z <= maxCorner.z; z++) {
				boolean ZDEC = z == minCorner.z;
				boolean ZINC = z == maxCorner.z;
				for (int y = minCorner.y; y <= maxCorner.y; y++) {
					boolean YDEC = y == minCorner.y;
					boolean YINC = y == maxCorner.y;
					
					Direction[] wallConf = makeDirArray(XINC, XDEC, ZINC, ZDEC, YINC, YDEC);
					w.getBlock(new Coord(x, y, z)).addComponent(new ComponentWall(wallConf, m));
				}
			}
		}
	}
	
	public static Direction[] makeDirArray(boolean XINC, boolean XDEC, boolean ZINC, boolean ZDEC, boolean YINC, boolean YDEC) {
		ArrayList<Direction> dirArr = new ArrayList<>();
		if (XINC) { dirArr.add(Direction.XINC); }
		if (XDEC) { dirArr.add(Direction.XDEC); }
		if (YINC) { dirArr.add(Direction.YINC); }
		if (YDEC) { dirArr.add(Direction.YDEC); }
		if (ZINC) { dirArr.add(Direction.ZINC); }
		if (ZDEC) { dirArr.add(Direction.ZDEC); }
		return dirArr.toArray(new Direction[dirArr.size()]);
	}
}
