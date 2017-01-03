package org.jmt.starfort.world.entity.ai;

import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;

import org.jmt.starfort.processor.Processor;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.controller.ControllerTask;
import org.jmt.starfort.world.entity.IEntity;
import org.jmt.starfort.world.entity.ai.ITask.TaskState;

/**
 * The AI for an entity
 * 
 * Hopefully this doensn't get confused with an AI Entity 
 * if one is ever created
 * 
 * @author Jediminer543
 *
 */
public class EntityAI {

	ITask t = null;
	FutureTask<ITask> futureT = null;

	public void tick(World w, Coord c, IEntity ie) {
		if (t == null && futureT == null) {
			futureT = (FutureTask<ITask>) w.getController(ControllerTask.class).getPerformableTask(ie);
			if (futureT != null) { // catches event where world lacks task 
				Processor.addRequest(futureT);
			}
		}
		if (futureT != null && futureT.isDone()) {
			try {
				t = futureT.get(100, TimeUnit.MICROSECONDS);
				futureT = null;
			} catch (InterruptedException | ExecutionException
					| TimeoutException e) {
				e.printStackTrace();
			}
		}
		if (t != null) {
			if (t.getTaskState() == TaskState.COMPLETE) {
				t = null;
			} else {
				t.tickTask(w, ie, c);
			}
		}
		
	}
}
