package org.jmt.starfort.game.entity.human;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.jmt.starfort.game.entity.EntityHumanoid;
import org.jmt.starfort.pathing.bruteforce.IPassageCallback;
import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.util.NavContext;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponentUpDown;
import org.jmt.starfort.world.entity.EntityAICapabilities;
import org.jmt.starfort.world.entity.IEntityAI;
import org.jmt.starfort.world.entity.ai.AIUtil.MoveState;
import org.jmt.starfort.world.entity.ai.ITask;
import org.jmt.starfort.world.entity.organs.IOrgan;
import org.jmt.starfort.world.material.IMaterial;
import org.jmt.starfort.world.material.MaterialRegistry;

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


	public void tick(Object... args) {
			World w = (World) args[0];
			Coord c = (Coord) args[1];
			ai.tickEntityAI(w, c, this);
	}
	
	ComplexRunnable tick = this::tick;

	final EntityHuman parent = this;

	private IEntityAI ai = new IEntityAI () {

		/**
		 * 
		 */
		private static final long serialVersionUID = -953252680971476860L;
		
		ITask task = null;
		MoveState ms = new MoveState();
		{
			ms.maxWait = 5;
		}

		@Override
		public EntityAICapabilities getEntityAICapibilities() {
			return null;
		}

		@Override
		public IPassageCallback getEntityAIPassageCallback() {
			return parent::passageCallback;
		}

		@Override
		public ITask getEntityAITask() {
			return task;
		}

		@Override
		public ITask setEntityAITask(ITask task) {
			ITask old = task;
			this.task = task;
			return old;
		}

		@Override
		public MoveState getEntityAIMoveState() {
			return ms;
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
		return MaterialRegistry.getMaterial("Bone");
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
		return ai;
	}
}
