package org.jmt.starfort.world.entity.aiold;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.controller.IController;
import org.jmt.starfort.world.entity.IEntity;

public class ControllerTask implements IController {

	ArrayList<ITask> worldTasks = new ArrayList<>();
	CopyOnWriteArrayList<ITask> commandTasks = new CopyOnWriteArrayList<>();
	
	long maxLen = 50000;
	int lastX, lastY, lastZ; 
	boolean finished;
	
	@Override
	public ComplexRunnable getTick() {
		return new ComplexRunnable() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -1939983941083200800L;

			@Override
			public void run(Object... args) {
				World w = (World)args[0];
				if (finished) {
					int[] bounds = w.getBounds(true);
					long startTime = System.nanoTime();
					for (int x = bounds[0]; x < bounds[3]; x++) {
						for (int y = bounds[1]; y < bounds[4]; y++) {
							for (int z = bounds[2]; z < bounds[5]; z++) {
								if (maxLen > System.nanoTime() - startTime) {
									lastX = x;
									lastY = y;
									lastZ = z;
									finished = false;
								} else {
									for (IComponentTaskedOld c: w.getBlockNoAdd(new Coord(x, y, z)).getCompInstances(IComponentTaskedOld.class)) {
										worldTasks.addAll(Arrays.asList(c.getComponentTasks()));
									}
								}
							}
						}
					}
				} else {
					int[] bounds = w.getBounds(false);
					long startTime = System.nanoTime();
					for (int x = lastX; x < bounds[3]; x++) {
						for (int y = lastY; y < bounds[4]; y++) {
							for (int z = lastZ; z < bounds[5]; z++) {
								if (maxLen > System.nanoTime() - startTime) {
									lastX = x;
									lastY = y;
									lastZ = z;
									finished = false;
								} else {
									if (w.getBlockNoAdd(new Coord(x, y, z)) != null)
									for (IComponentTaskedOld c: w.getBlockNoAdd(new Coord(x, y, z)).getCompInstances(IComponentTaskedOld.class)) {
										worldTasks.addAll(Arrays.asList(c.getComponentTasks()));
									}
								}
							}
						}
					}
				}
				if (commandTasks != null) {
					for (ITask task : commandTasks) {
						if (task.getTaskState() == ITask.TaskState.COMPLETE) {
							commandTasks.remove(task);
						}
					}
				}
			}
		};
	}
	
	public void addCommandTask(ITask t) {
		commandTasks.add(t);
	}
	
	public RunnableFuture<ITask> getPerformableTask(final IEntity e) {
		return new FutureTask<ITask>(new Callable<ITask>() {

			@Override
			public ITask call() throws Exception {
				ITask highestPerformableTask = null;
				int highestTaskScore = Integer.MIN_VALUE;
				
				// Entity not required to return valid result, without this check system is GIEO (Garbage in, Exception out) 
				// where as now it is GIGO
				if (e.getEntityTaskList() != null) {
				for (ITask task : e.getEntityTaskList()) {
					if (task.getTaskPriority() > highestTaskScore && task.canTaskPerform(e)) {
						highestTaskScore = task.getTaskPriority();
						highestPerformableTask = task;
					}
				}}
				// FIRST RULE OF CONCURRENCY: NEVER TRUST CONCURRENCY
				// IT NEVER CONCENTRATES ON A SINGLE TASK
				if (commandTasks != null) {
				for (ITask task : commandTasks) {
					if (task.getTaskPriority() > highestTaskScore && task.canTaskPerform(e)) {
						highestTaskScore = task.getTaskPriority();
						highestPerformableTask = task;
					}
				}
				}
				// FIRST RULE OF CONCURRENCY: NEVER TRUST CONCURRENCY
				// IT NEVER CONCENTRATES ON A SINGLE TASK
				if (worldTasks != null) {
				for (ITask task : worldTasks) {
					if (task.getTaskPriority() > highestTaskScore && task.canTaskPerform(e)) {
						highestTaskScore = task.getTaskPriority();
						highestPerformableTask = task;
					}
				}
				}
				return highestPerformableTask;
			}
		});
	}
}
