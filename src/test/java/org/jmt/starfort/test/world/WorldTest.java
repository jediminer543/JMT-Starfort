package org.jmt.starfort.test.world;

import static org.junit.Assert.*;

import java.util.Random;

import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.block.Block;
import org.jmt.starfort.world.controller.IController;
import org.junit.Test;

public class WorldTest extends World {

	@Test
	public void testGetBlockNoAdd() {
		World w = new World();
		Coord notNull = new Coord(0,0,0);
		Block tgtBlock = new Block();
		
		w.getBlocks().put(notNull, tgtBlock);
		
		Coord shouldNull = new Coord(1, 0, 0);
		
		assertEquals("Testing World.getBlockNoAdd gets an existing block", tgtBlock, w.getBlockNoAdd(notNull));
		assertNull("Testing World.getBlockNoAdd doesn't create blocks where they dont exist", w.getBlockNoAdd(shouldNull));
		
	}
	
	@Test
	public void testGetBlock() {
		World w = new World();
		Coord notNull = new Coord(0,0,0);
		Block tgtBlock = new Block();
		
		w.getBlocks().put(notNull, tgtBlock);
		
		Coord alsoNotNull = new Coord(1, 0, 0);
		
		assertEquals("Testing World.getBlockNoAdd gets an existing block", tgtBlock, w.getBlock(notNull));
		assertNotNull("Testing World.getBlockNoAdd creates blocks where they dont exist", w.getBlock(alsoNotNull));
	}
	
	@Test
	public void testGetBlockLocation() {
		World w = new World();
		Coord notNull = new Coord(0,0,0);
		Block tgtBlock = new Block();
		w.getBlocks().put(notNull, tgtBlock);
		
		assertEquals("Test World.getBlockLocation gets location", notNull, w.getBlockLocation(tgtBlock));
		assertNull("Test World.getBlockLocation gets location", w.getBlockLocation(new Block()));
	}
	
	@Test
	public void testGetBounds() {
		World w = new World();
		Random r = new Random();
		
		int[] data = {r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt()};
		
		int maxx = Math.max(data[0], data[1]);
		int minx = Math.min(data[0], data[1]);
		int maxy = Math.max(data[2], data[3]);
		int miny = Math.min(data[2], data[3]);
		int maxz = Math.max(data[4], data[5]);
		int minz = Math.min(data[4], data[5]);
		
		w.getBlock(new Coord(maxx, miny, maxz));
		w.getBlock(new Coord(minx, maxy, maxz));
		w.getBlock(new Coord(maxx, maxy, maxz));
		w.getBlock(new Coord(maxx, maxy, minz));
		w.getBlock(new Coord(minx, miny, minz));
		
		int[] bounds = w.getBounds(true);
		
		assertEquals("Check worldBounds minx", minx, bounds[0]);
		assertEquals("Check worldBounds maxx", maxx, bounds[3]);
		assertEquals("Check worldBounds miny", miny, bounds[1]);
		assertEquals("Check worldBounds maxy", maxy, bounds[4]);
		assertEquals("Check worldBounds minz", minz, bounds[2]);
		assertEquals("Check worldBounds maxz", maxz, bounds[5]);
		
		
		assertEquals("Check for unnececary recalculation", bounds, w.getBounds(false));
	}
	
	public static class TestController implements IController {

		@Override
		public ComplexRunnable getTick() {
			return null;
		}
		
	}
	
	@Test
	public void test() {
		World w = new World();
		
		int initialSize = w.getController().size();
		
		w.getController(TestController.class);
		
		assertEquals("Check for controller creation", initialSize+1, w.getController().size());
		
		w.getController(TestController.class);
		
		assertEquals("Check for controller non-duplication", initialSize+1, w.getController().size());
	}

}
