package org.jmt.starfort.samples;

import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.CoordRange;

public class CoordRangeTest {

	public static void main(String[] args) {
		
		CoordRange cr = new CoordRange(new Coord(0,0,0), new Coord(2,2,2));
		System.out.println(cr.max);
		for (Coord c : cr) {
			System.out.println(c);
		}
	}
}
