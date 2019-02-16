package org.jmt.starfort.game.entity;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import org.jmt.starfort.pathing.bruteforce.IPassageCallback;
import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.util.NavContext;
//import org.jmt.starfort.world.TickRequest;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponentUpDown;
import org.jmt.starfort.world.entity.IEntity;
import org.jmt.starfort.world.entity.IEntityAI;
import org.jmt.starfort.world.entity.ai.AIUtil;
import org.jmt.starfort.world.entity.ai.AIUtil.MoveState;
import org.jmt.starfort.world.entity.ai.CannotPathException;
import org.jmt.starfort.world.entity.organs.IOrgan;
import org.jmt.starfort.world.material.IMaterial;
import org.jmt.starfort.world.material.MaterialRegistry;

public class EntityDrone implements IEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6954828635651637329L;
	
	static int numberCumalative = 0;
	int number;
	{
		number = numberCumalative;
		numberCumalative++;
	}
	
	//public transient Path p;
	//public transient RunnableFuture<Path> futurePath;
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
		return "Drony McDroneface " + number;
	}

	@Override
	public IMaterial getComponentMaterial() {
		return MaterialRegistry.getMaterial("Bone"); // JUST HERE FOR DEBUG TODO FIX
	}

	public Coord lastPos;
	
	public MoveState ms = new MoveState();
	{
		ms.maxWait = 2;
	}
	
	private void tick(Object... args) {
			EntityDrone parent = this;
			World w = (World) args[0];
			Coord c = (Coord) args[1];
			//TickRequest tr = (TickRequest) args[2];
			if (c == null) {
				//IDK WHAT HAPPENED HERE
				throw new IllegalStateException("SOMETHING WENT HORRIBLY WRONG HERE");
			}
			try {
				if (AIUtil.controledEntityMoveTo(w, c, parent, targets.getFirst(), ms)) {
					targets.addLast(targets.pop());
				}
			} catch (CannotPathException e) {
				e.printStackTrace();
				targets.addLast(targets.pop());
			}	
			/*
			if (lastPos == null) {
				lastPos = c;
			}
			if (!c.equals(lastPos)) {
				p = null;
				futurePath = null;
				lastPos = c;
			}
			if (p == null && futurePath == null) {
				futurePath = BruteforcePather.pathBetweenAsync(c, parent.targets.getFirst(), w, parent.getEntityPassageCallback());
				Processor.addRequest(futurePath);
				p = null;
			}
			if (c.equals(targets.getFirst())) {
				targets.addLast(targets.pop());
			}
			if (futurePath != null && futurePath.isDone()) {
				try {
					p = futurePath.get(100, TimeUnit.MICROSECONDS);
					futurePath = null;
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
				Coord dst = c.add(parent.p.pop().getDir());
				if (w.moveComponent(parent, c, dst)) {
					lastPos = dst;
				} else {
					p = null;
					futurePath = null;
				}
				
			}
			*/
	};
	
	ComplexRunnable tick = this::tick;
	
	@Override
	public ComplexRunnable getTick() {
		return this.tick;
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
	
	IPassageCallback passageCallback = this::passageCallback;
	
	@Override
	public IPassageCallback getEntityPassageCallback() {
		return this::passageCallback;
	}

	@Override
	public IEntityAI getEntityAI() {
		return new EntityAI(this, null) {
			@Override
			public IPassageCallback getEntityAIPassageCallback() {
				return passageCallback;
			}
		};
	}

}
