package org.jmt.starfort.game.components;

import java.util.Map;

import org.jmt.starfort.util.Direction;
import org.jmt.starfort.util.InlineFunctions;
import org.jmt.starfort.util.NavContext;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentBlocking;
import org.jmt.starfort.world.component.IComponentDamagable;

public class ComponentWall implements IComponent, IComponentBlocking, IComponentDamagable {

	Direction[] blockedDirs;
	int wallHealth;
	//TODO REMOVE COLOUR
	float[] colour;
	public ComponentWall(Direction[] blockedDirs, float[] colour) {
		this.blockedDirs = blockedDirs;
		this.colour = colour;
	}
	
	@Override
	public int getComponentHealth() {
		return 0;
	}

	@Override
	public int getComponentMaxHealth() {
		return 0;
	}

	@Override
	public void modComponentHealth() {
		
	}

	@Override
	public Map<NavContext, Direction[]> getBlockedDirs() {
		return InlineFunctions.inlineMap(InlineFunctions.inlineKey(NavContext.Physical, blockedDirs), InlineFunctions.inlineKey(NavContext.Fluidic, blockedDirs), InlineFunctions.inlineKey(NavContext.Gasseous, blockedDirs));
	}

	@Override
	public String getComponentName() {
		return null;
	}
	
	//TODO DELETE
	public float[] getColor() {
		return colour;
	}
	
	
}
