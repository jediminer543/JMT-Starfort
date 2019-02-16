package org.jmt.starfort.game.components.debug;

import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.TemporalBlacklist;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.component.IComponentTasked;
import org.jmt.starfort.world.controller.ControllerLookup;
import org.jmt.starfort.world.entity.IEntity;
import org.jmt.starfort.world.entity.ai.CannotPathException;
import org.jmt.starfort.world.entity.ai.ITask;
import org.jmt.starfort.world.entity.ai.TaskState;
import org.jmt.starfort.world.material.IMaterial;
import org.jmt.starfort.world.material.MaterialRegistry;

/**
 * A flag for debugging
 * 
 * Used for testing path-finding and AI task collection
 * 
 * As is most of the stuff around here, this is TODO
 * 
 * @author jediminer543
 *
 */
public class ComponentDebugFlag implements IComponent, IComponentTasked {

	/**
	 * SERIAL ID
	 */
	private static final long serialVersionUID = 6931361998394771002L;

	TemporalBlacklist<IEntity> pathingBlacklist = new TemporalBlacklist<>();
	
	String flagid; 
	int pri;
	
	public ComponentDebugFlag(String flagid, int pri) {
		this.flagid = flagid;
		this.pri = pri;
	}

	@Override
	public String getComponentName() {
		return "Debug Flag";
	}

	@Override
	public IMaterial getComponentMaterial() {
		return MaterialRegistry.getMaterial("Debug");
	}

	long sleepTime = 10000;
	long lastTime = System.currentTimeMillis() - sleepTime;
	
	
	@Override
	public int avaliableTaskGeneratorTasks() {
		//System.out.println(lastTime+sleepTime + " : " + System.currentTimeMillis());
		return lastTime+sleepTime <= System.currentTimeMillis() ? 1 : 0;
	}

	@Override
	public boolean isTaskGeneratorCompletable(IEntity entity) {
		if (pathingBlacklist.contains(entity)) return false;
		return true;
	}

	@Override
	public int getTaskGeneratorHighestPriority() {
		return pri;
	}

	private ComponentDebugFlag beMe = this;
	
	private class Task implements ITask {

		private TaskState state = TaskState.CONTINUE;
		private Coord target = null;
		
		@Override
		public void execute(World w, Coord cur, IEntity ie) {
			if (state == TaskState.DONE) return;
			if (target == null) {
				target = w.getController(ControllerLookup.class).findComponentInstance(beMe);
			}
			if (target != null) {
				try {
					if (ie.getEntityAI().moveTo(w, cur, ie, target)) {
						state = TaskState.DONE;
					}
				} catch (CannotPathException e) {
					pathingBlacklist.add(ie);
					state = TaskState.ERROR;
				}
			}
		}

		@Override
		public TaskState getTaskState() {
			return state;
		}
		
	}
	
	@Override
	public ITask getTaskGeneratorTask(IEntity entity) {
		lastTime = System.currentTimeMillis();
		return new Task();
	}

}
