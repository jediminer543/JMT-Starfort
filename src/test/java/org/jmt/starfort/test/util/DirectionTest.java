package org.jmt.starfort.test.util;

import static org.junit.Assert.*;

import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.Direction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DirectionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	@Test
	public void testInverse() {
		Direction dir1 = Direction.YDEC;
		Direction dir2 = dir1.inverse();
		assertEquals("Test Double Inverse", dir1, dir2.inverse()); 
	}

	@Test
	public void testInverseOf() {
		Direction dir1 = Direction.XDEC;
		Direction dir2 = Direction.inverseOf(dir1);
		Coord resultant = dir1.getDir().add(dir2.getDir());
		assertEquals("Checking X is zero", 0, resultant.x);
		assertEquals("Checking Y is zero", 0, resultant.y);
		assertEquals("Checking Z is zero", 0, resultant.z);
	}

	@Test
	public void testXzFlip() {
		Direction dir1 = Direction.values()[0];
		Direction dir2 = Direction.xzFlip(dir1);
		Coord resultant = dir1.getDir().abs().addM(dir2.getDir().abs());
		assertEquals("Checking X and Z are equal", resultant.x, resultant.z);
	}
}
