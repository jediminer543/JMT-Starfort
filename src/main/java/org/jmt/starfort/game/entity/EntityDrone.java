package org.jmt.starfort.game.entity;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jmt.starfort.pathing.bruteforce.BruteforcePather;
import org.jmt.starfort.pathing.bruteforce.IPassageCallback;
import org.jmt.starfort.pathing.bruteforce.Path;
import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.processor.Processor;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.util.NavContext;
//import org.jmt.starfort.world.TickRequest;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponentUpDown;
import org.jmt.starfort.world.entity.IEntity;
import org.jmt.starfort.world.entity.IEntityAI;
import org.jmt.starfort.world.entity.aiold.ITask;
import org.jmt.starfort.world.entity.organs.IOrgan;
import org.jmt.starfort.world.material.IMaterial;

public class EntityDrone implements IEntity {

	Path p;
	RunnableFuture<Path> futurePath;
	Deque<Coord> targets = new ArrayDeque<Coord>();
	{
		targets.addLast(new Coord(0, 0, 3));
		targets.addLast(new Coord(-1, 0, 0));
		targets.addLast(new Coord(0, 0, -1));
		targets.addLast(new Coord(2, 0, 0));
		targets.addLast(new Coord(6, 0, 6));
	}
	
	@Override
	public String getComponentName() {
		return "Drony McDroneface";
	}

	@Override
	public IMaterial getComponentMaterial() {
		return null;
	}

	ComplexRunnable tick = (Object... args) -> {
			EntityDrone parent = this;
			World w = (World) args[0];
			Coord c = (Coord) args[1];
			//TickRequest tr = (TickRequest) args[2];
			System.out.println("Processing");
			if (parent.p == null && parent.futurePath == null) {
				parent.futurePath = BruteforcePather.pathBetweenAsync(c, parent.targets.getFirst(), w, parent.getEntityPassageCallback());
				Processor.addRequest(parent.futurePath);
				parent.p = null;
			}
			if (c.equals(parent.targets.getFirst())) {
				parent.targets.addLast(parent.targets.pop());
			}
			if (parent.futurePath != null && parent.futurePath.isDone()) {
				try {
					parent.p = parent.futurePath.get(100, TimeUnit.MICROSECONDS);
					parent.futurePath = null;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
				}
			}
			if (parent.p != null && parent.p.remaining() <= 0 && parent.futurePath == null) {
				parent.futurePath = BruteforcePather.pathBetweenAsync(c, parent.targets.getFirst(), w, parent.getEntityPassageCallback());
				Processor.addRequest(parent.futurePath);
				parent.p = null;
			} else if (parent.p != null && parent.p.remaining() > 0) {
				Coord dst = c.addR(parent.p.pop().getDir());
				w.moveComponent(parent, c, dst);
			}
	};
	
	@Override
	public ComplexRunnable getTick() {
		return tick;
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

	public boolean passageCallback(World w, Coord src, Direction dir) {
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
