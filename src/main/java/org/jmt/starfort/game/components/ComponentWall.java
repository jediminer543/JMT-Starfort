
package org.jmt.starfort.game.components;

import java.util.Map;

import org.jmt.starfort.util.Direction;
import org.jmt.starfort.util.InlineFunctions;
import org.jmt.starfort.util.NavContext;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentBlocking;
import org.jmt.starfort.world.component.IComponentDamagable;
import org.jmt.starfort.world.material.IMaterial;

public class ComponentWall implements IComponent, IComponentBlocking, IComponentDamagable {

	Direction[] blockedDirs;
	int wallHealth, wallMaxHealth;
	IMaterial mat;
	
	public ComponentWall(Direction[] blockedDirs, IMaterial mat) {
		this.blockedDirs = blockedDirs;
		this.mat = mat;
		wallMaxHealth = wallHealth = Math.round(mat.getMaterialHardness() * 25);
	}
	
	@Override
	public int getComponentHealth() {
		return wallHealth;
	}

	@Override
	public int getComponentMaxHealth() {
		return wallMaxHealth;
	}

	@Override
	public void modComponentHealth(int delta) {
		wallHealth += delta;
	}

	@Override
	public Map<NavContext, Direction[]> getComponentBlockedDirs() {
		return InlineFunctions.inlineMap(InlineFunctions.inlineKey(NavContext.Physical, blockedDirs), InlineFunctions.inlineKey(NavContext.Fluidic, blockedDirs), InlineFunctions.inlineKey(NavContext.Gasseous, blockedDirs));
	}

	@Override
	public String getComponentName() {
		return "Wall";
	}

	@Override
	public IMaterial getComponentMaterial() {
		return mat;
	}

	@Override
	public Direction[] getComponentDirections() {
		return blockedDirs;
	}
	
}
