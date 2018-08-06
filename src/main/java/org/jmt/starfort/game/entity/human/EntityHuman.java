package org.jmt.starfort.game.entity.human;

import java.util.List;

import org.jmt.starfort.game.entity.EntityHumanoid;
import org.jmt.starfort.pathing.bruteforce.IPassageCallback;
import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.util.NavContext;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponentUpDown;
import org.jmt.starfort.world.entity.IEntityAI;
import org.jmt.starfort.world.entity.organs.IOrgan;
import org.jmt.starfort.world.material.IMaterial;

public class EntityHuman extends EntityHumanoid {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1713245705625921265L;
	String name;
	
	public EntityHuman(String name) {
		this.name = name;
	}
	
	@Override
	public String getEntityName() {
		return name;
	}

	@Override
	public String getComponentName() {
		return "Human";
	}

	final EntityHuman parent = this;
	transient ComplexRunnable tick = new ComplexRunnable() {
			
			/**
		 * 
		 */
		private static final long serialVersionUID = 6525438261145656859L;

			@Override
			public void run(Object... args) {
				//TODO FIX
			}
		};
	
	@Override
	public ComplexRunnable getTick() {
		return tick;
	}

	@Override
	public IOrgan getEntityOrganBody() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public IMaterial getComponentMaterial() {
		return null;
	}

	public boolean passageCallback(World w, Coord src, Direction dir) {
		if (w.getBlockNoAdd(src.add(dir.getDir())) == null || w.getBlockNoAdd(src) == null) {
			return false;
		}
		if (dir != Direction.YINC && dir != Direction.YDEC) {
			if (w.getBlockNoAdd(src.add(dir.getDir())).getBlockedDirs(NavContext.Physical).contains(dir.inverse()) || w.getBlockNoAdd(src.add(dir.getDir())).getBlockedDirs(NavContext.Physical).contains(Direction.SELFFULL) 
					|| w.getBlockNoAdd(src.get()).getBlockedDirs(NavContext.Physical).contains(dir)) {
					return false;
			}
			return true;
		} else {
			List<IComponentUpDown> UDCL = null;
			if (!(UDCL = w.getBlockNoAdd(src).getCompInstances(IComponentUpDown.class)).isEmpty()) {
				for (IComponentUpDown UDC : UDCL) {
					if (UDC.canUp() && dir == Direction.YINC)
						return true;
					if (UDC.canDown() && dir == Direction.YDEC)
						return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public IPassageCallback getEntityPassageCallback() {
		return this::passageCallback;
	}

	@Override
	public IEntityAI getEntityAI() {
		// TODO Auto-generated method stub
		return null;
	}

}
