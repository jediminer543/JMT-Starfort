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
import org.jmt.starfort.world.entity.ai.EntityAI;
import org.jmt.starfort.world.entity.ai.ITask;
import org.jmt.starfort.world.entity.ai.Task;
import org.jmt.starfort.world.entity.ai.subtask.TaskMove;
import org.jmt.starfort.world.entity.organs.IOrgan;
import org.jmt.starfort.world.material.IMaterial;

public class EntityHuman extends EntityHumanoid {

	String name;
	EntityAI entityAI = new EntityAI();
	IPassageCallback pc = new IPassageCallback() {
		
		@Override
		public boolean canPass(World w, Coord src, Direction dir) {
			if (dir != Direction.YINC && dir != Direction.YDEC) {
				if (w.getBlock(src.addR(dir.getDir())).getBlockedDirs(NavContext.Physical).contains(dir.inverse()) || w.getBlock(src.addR(dir.getDir())).getBlockedDirs(NavContext.Physical).contains(Direction.SELFFULL) 
						|| w.getBlock(src.get()).getBlockedDirs(NavContext.Physical).contains(dir)) {
						return false;
				}
				return true;
			} else {
				List<IComponentUpDown> UDCL = null;
				if (!(UDCL = w.getBlock(src).getCompInstances(IComponentUpDown.class)).isEmpty()) {
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
	};
	
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
	ComplexRunnable tick = new ComplexRunnable() {
			
			@Override
			public void run(Object... args) {
				World w = (World) args[0];
				Coord c = (Coord) args[1];
				entityAI.tick(w, c, parent);
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

	@Override
	public ITask[] getEntityTaskList() {
		Task t = new Task(name, 10, new ITask[] { new TaskMove(new Coord(6, 0, 6))});
		
		return new ITask[] {t};
	}

	@Override
	public IPassageCallback getEntityPassageCallback() {
		return pc;
	}

}
