package org.jmt.starfort.world.entity.ai;

import java.util.concurrent.RunnableFuture;

import org.jmt.starfort.processor.Processor;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.controller.ControllerTask;
import org.jmt.starfort.world.entity.IEntity;

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
	RunnableFuture<ITask> futureT = null;
	
	public EntityAI() {
		//TODO
	}
	
	public void tick(World w, IEntity ie) {
		if (t == null && futureT == null) {
			futureT = ((ControllerTask) w.getController(ControllerTask.class)).getPerformableTask(ie);
			Processor.addRequest(futureT);
		} else {
			
		}
	}
}
