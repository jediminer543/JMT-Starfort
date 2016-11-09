package org.jmt.starfort.world.controller;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.world.entity.IEntity;
import org.jmt.starfort.world.entity.ai.ITask;

public class ControllerTask implements IController {

	ArrayList<ITask> worldTasks = new ArrayList<>();
	
	@Override
	public ComplexRunnable getTick() {
		return new ComplexRunnable() {
			
			@Override
			public void run(Object... args) {
				
				
			}
		};
	}
	
	public RunnableFuture<ITask> getPerformableTask(final IEntity e) {
		return new FutureTask<ITask>(new Callable<ITask>() {

			@Override
			public ITask call() throws Exception {
				ITask highestPerformableTask = null;
				int highestTaskScore = Integer.MIN_VALUE;
				for (ITask task : e.getEntityTaskList()) {
					if (task.getTaskPriority() > highestTaskScore && task.canTaskPerform(e)) {
						highestTaskScore = task.getTaskPriority();
						highestPerformableTask = task;
					}
				}
				for (ITask task : worldTasks) {
					if (task.getTaskPriority() > highestTaskScore && task.canTaskPerform(e)) {
						highestTaskScore = task.getTaskPriority();
						highestPerformableTask = task;
					}
				}
				return highestPerformableTask;
			}
		});
	}
}
