package org.jmt.starfort.world.component.designator;

import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.TemporalBlacklist;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.controller.ControllerLookup;
import org.jmt.starfort.world.entity.IEntity;
import org.jmt.starfort.world.entity.ai.CannotPathException;
import org.jmt.starfort.world.entity.ai.ITask;
import org.jmt.starfort.world.entity.ai.TaskState;
import org.jmt.starfort.world.material.IMaterial;

public class DesignatorConstruct implements IComponentDesignator {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1836565121577992454L;
	public int work = 0;
	public int workMax;
	IComponent source;
	
	TemporalBlacklist<IEntity> pathingBlacklist = new TemporalBlacklist<>();
	
	public DesignatorConstruct(IComponent source) {
		this(source, 0);
	}
	
	public DesignatorConstruct(IComponent source, int work) {
		this.source = source;
		this.workMax = work;
	}
	
	@Override
	public String getComponentName() {
		return "Construct Designator";
	}

	@Override
	public IMaterial getComponentMaterial() {
		return null;
	}

	@Override
	public boolean isTaskGeneratorCompletable(IEntity entity) {
		if (pathingBlacklist.contains(entity)) return false;
		return true; //TODO FIXME
	}

	@Override
	public int avaliableTaskGeneratorTasks() {
		return isDesignatorComplete() ? 0 : 1;
	}

	@Override
	public int getTaskGeneratorHighestPriority() {
		return 100;
	}

	private DesignatorConstruct thiss = this;
	
	@Override
	public ITask getTaskGeneratorTask(IEntity entity) {
		return new ITask() {
			
			TaskState s = TaskState.CONTINUE;
			Coord target = null;
			
			@Override
			public TaskState getTaskState() {
				return s;
			}
			
			@Override
			public void execute(World w, Coord cur, IEntity ie) {
				if (s == TaskState.DONE) return;
				else {
					if (target == null) {
						target = w.getController(ControllerLookup.class).findComponentInstance(thiss);
					} else {
						try {
							if (ie.getEntityAI().moveToRange(w, cur, ie, target, new Coord(1, 1, 1))) {
								if (doDesignatorWork(w, target, ie, cur, 1)) {
									s = TaskState.DONE;
								}
							}
						} catch (CannotPathException e) {
							pathingBlacklist.add(ie);
							s = TaskState.ERROR;
						}
					}
				}
			}
		};
	}

	@Override
	public boolean isDesignatorComplete() {
		return done;
	}

	@Override
	public int getDesignatorMaxWork() {
		return workMax;
	}

	@Override
	public int getDesignatorWork() {
		return work;
	}

	private boolean done = false;
	
	@Override
	public boolean doDesignatorWork(World w, Coord c, IEntity ie, Coord ec, int amount) {
		work += amount;
		if (work >= workMax && !done) {
			done = true;
			w.getBlock(c).addComponent(source);
		}
		return done;
	}


}
