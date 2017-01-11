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
import org.jmt.starfort.world.TickRequest;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponentUpDown;
import org.jmt.starfort.world.entity.IEntity;
import org.jmt.starfort.world.entity.ai.ITask;
import org.jmt.starfort.world.entity.organs.IOrgan;
import org.jmt.starfort.world.material.IMaterial;

public class EntityDrone implements IEntity {

	public final class Dataset {
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
		
	}
	
	Dataset ds = new Dataset();
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
	
	@Override
	public String getComponentName() {
		return "Drony McDroneface";
	}

	@Override
	public IMaterial getComponentMaterial() {
		return null;
	}

	final EntityDrone parentt = this;
	ComplexRunnable tick = new ComplexRunnable() {
		EntityDrone parent = parentt;
		@Override
		public void run(Object... args) {
			World w = (World) args[0];
			Coord c = (Coord) args[1];
			TickRequest tr = (TickRequest) args[2];
			//System.out.println("Processing");
			if (parent.ds.p == null && parent.ds.futurePath == null) {
				parent.ds.futurePath = BruteforcePather.pathBetweenAsync(c, parent.ds.targets.getFirst(), w, pc);
				Processor.addRequest(parent.ds.futurePath);
				parent.ds.p = null;
			}
			if (c.equals(parent.ds.targets.getFirst())) {
				parent.ds.targets.addLast(parent.ds.targets.pop());
			}
			if (parent.ds.futurePath != null && parent.ds.futurePath.isDone()) {
				try {
					parent.ds.p = parent.ds.futurePath.get(100, TimeUnit.MICROSECONDS);
					parent.ds.futurePath = null;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
				}
			}
			if (parent.ds.p != null && parent.ds.p.remaining() <= 0 && parent.ds.futurePath == null) {
				parent.ds.futurePath = BruteforcePather.pathBetweenAsync(c, parent.ds.targets.getFirst(), w, pc);
				Processor.addRequest(parent.ds.futurePath);
				parent.ds.p = null;
			} else if (parent.ds.p != null && parent.ds.p.remaining() > 0) {
				Coord dst = c.addR(parent.ds.p.pop().getDir());
				w.moveComponent(parent, c, dst);
			}
			//System.out.println("Drone Proccecing cycle complete");
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

	@Override
	public IPassageCallback getEntityPassageCallback() {
		return pc;
	}

}
