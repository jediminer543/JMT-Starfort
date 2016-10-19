package org.jmt.starfort.game.entity;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import org.jmt.starfort.pathing.bruteforce.BruteforcePather;
import org.jmt.starfort.pathing.bruteforce.IPassageCallback;
import org.jmt.starfort.pathing.bruteforce.Path;
import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.util.NavContext;
import org.jmt.starfort.world.TickRequest;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentUpDown;
import org.jmt.starfort.world.entity.IEntity;
import org.jmt.starfort.world.entity.ai.ITask;
import org.jmt.starfort.world.entity.organs.IOrgan;
import org.jmt.starfort.world.material.IMaterial;

public class EntityDrone implements IEntity {

	public final class Dataset {
		Path p;
		Deque<Coord> targets = new ArrayDeque<Coord>();
		{
			targets.addLast(new Coord(0, 0, 3));
			targets.addLast(new Coord(-1, 0, 0));
			targets.addLast(new Coord(0, 0, -1));
			targets.addLast(new Coord(1, 0, 0));
			targets.addLast(new Coord(6, 0, 6));
		}
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
					List<IComponent> UDCL = null;
					if (!(UDCL = w.getBlock(src).getCompInstances(IComponentUpDown.class)).isEmpty()) {
						for (IComponent c : UDCL) {
							IComponentUpDown UDC = (IComponentUpDown) c;
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
	}
	
	Dataset ds = new Dataset();
	
	@Override
	public String getComponentName() {
		return "Drony McDroneface";
	}

	@Override
	public IMaterial getComponentMaterial() {
		return null;
	}

	@Override
	public ComplexRunnable getTick() {
		final EntityDrone parentt = this;
		return new ComplexRunnable() {
			EntityDrone parent = parentt;
			@Override
			public void run(Object... args) {
				World w = (World) args[0];
				Coord c = (Coord) args[1];
				TickRequest tr = (TickRequest) args[2];
				if (parent.ds.p == null) {
					parent.ds.p = BruteforcePather.pathBetween(c, parent.ds.targets.getFirst(), w, parent.ds.pc);
				}
				if (c.equals(parent.ds.targets.getFirst())) {
					parent.ds.targets.addLast(parent.ds.targets.pop());
				}
				if (parent.ds.p.remaining() <= 0) {
					parent.ds.p = BruteforcePather.pathBetween(c, parent.ds.targets.getFirst(), w, parent.ds.pc);
				} else {
					w.getBlockNoAdd(c).removeComponent(parent);
					Coord dst = c.addR(parent.ds.p.pop().getDir());
					w.getBlock(dst).addComponent(parent);
					tr.move(c, dst, this);
				}
				//System.out.println("Drone Proccecing cycle complete");
			}
		};
	}

	@Override
	public String getEntityName() {
		return "Drony McDroneface";
	}

	@Override
	public IOrgan getEntityOrganBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITask[] getEntityTaskList() {
		// TODO Auto-generated method stub
		return null;
	}

}
