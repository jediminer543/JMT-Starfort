package org.jmt.starfort.voxel.component;

import org.jmt.starfort.util.Coord;

public interface IVoxelComponent {

	public int getVoxelPriority();
	
	public int getVoxelColor(Coord pos);
	
}
