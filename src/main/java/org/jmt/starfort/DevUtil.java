package org.jmt.starfort;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jmt.starfort.game.components.ComponentStairs;
import org.jmt.starfort.game.components.ComponentWall;
import org.jmt.starfort.game.components.debug.ComponentDebugFlag;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.CoordRange;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.util.InlineFunctions;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.designator.impl.DesignatorConstruct;
import org.jmt.starfort.world.component.designator.impl.DesignatorReplace;
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
	
	public static void makeRoomDesignators(World w, IMaterial m, Coord minCorner, Coord maxCorner) {
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
					w.getBlock(new Coord(x, y, z)).addComponent(new DesignatorConstruct(new ComponentWall(wallConf, m), 10));
				}
			}
		}
	}
	
	public static void makeCoridoor(World w, IMaterial m, Coord src, Direction dir, int length) {
		List<Direction> dirSetDefault = new ArrayList<>();
		dirSetDefault.addAll(Arrays.asList(new Direction[] {Direction.xzFlip(dir), Direction.inverseOf(Direction.xzFlip(dir)), Direction.YDEC, Direction.YINC}));
		Coord dest = src.add(dir.getDir().mult(length));
		for (Coord c : new CoordRange(src, dest)) {
			if (w.getBlockNoAdd(c) != null && w.getBlockNoAdd(c).getCompInstance(ComponentWall.class) != null) {
				ComponentWall wall = w.getBlockNoAdd(c).getCompInstance(ComponentWall.class);
				List<Direction> invDirSet = new ArrayList<>();
				if (!c.equals(src)) {
					invDirSet.add(dir.inverse());
				}
				if (!c.equals(dest)) {
					invDirSet.add(dir);
				}
				List<Direction> dirSet = InlineFunctions.inlineArrayList(wall.getComponentDirections());
				dirSet.removeAll(invDirSet);
				w.getBlock(c).addComponent(new DesignatorReplace(wall, new ComponentWall(dirSet.toArray(new Direction[dirSet.size()]), m), 10));
			} else {
				List<Direction> dirSet = new ArrayList<>(dirSetDefault);
				if (c.equals(src)) {
					dirSet.add(dir.inverse());
				}
				if (c.equals(dest)) {
					dirSet.add(dir);
				}
				w.getBlock(c).addComponent(new DesignatorConstruct(new ComponentWall(dirSet.toArray(new Direction[dirSet.size()]), m), 10));
			}
		}
	}
	
	public static void makeStaircase(World w, IMaterial m, Coord src, int length) {
		Coord dest = src.add(Direction.YINC.getDir().mult(length));
		if (length < 0) {
			Coord im = src;
			src = dest;
			dest = im;
		}
		for (Coord c : new CoordRange(src, dest)) {
			boolean up = !c.equals(dest);
			boolean down = !c.equals(src);
			w.getBlock(c).addComponent(new DesignatorConstruct(new ComponentStairs(m, up, down), 10));
		}
	}
	
	public static void makeFlag(World w, IMaterial m, Coord c, String id, int pri) {
		w.getBlock(c).addComponent(new DesignatorConstruct(new ComponentDebugFlag(id, pri), 10));
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
