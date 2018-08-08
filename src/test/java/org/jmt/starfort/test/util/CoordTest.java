package org.jmt.starfort.test.util;

import static org.junit.Assert.*;

import org.jmt.starfort.util.Coord;
import org.junit.Test;

public class CoordTest {

	@Test
	public void testStorage() {
		Coord c = new Coord(5, 2, 7);
		assertEquals(c.x, 5);
		assertEquals(c.y, 2);
		assertEquals(c.z, 7);
	}
	
	@Test
	public void testEquals() {
		Coord c = new Coord(5, 2, 7);
		Coord c1 = new Coord(5, 2, 7);
		Coord c2 = new Coord(5, 2, 8);
		assertTrue(c.equals(c1));
		assertFalse(c.equals(c2));
	}

	@Test
	public void testHash() {
		Coord c = new Coord(5, 2, 7);
		Coord c1 = new Coord(5, 2, 7);
		Coord c2 = new Coord(5, 2, 8);
		assertTrue(c.hashCode() == c1.hashCode());
		assertFalse(c.hashCode() == c2.hashCode());
	}
}
