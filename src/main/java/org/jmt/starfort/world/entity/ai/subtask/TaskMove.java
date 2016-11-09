package org.jmt.starfort.world.entity.ai.subtask;

import java.util.concurrent.RunnableFuture;

import org.jmt.starfort.pathing.bruteforce.Path;
import org.jmt.starfort.world.entity.IEntity;
import org.jmt.starfort.world.entity.ai.ITask;

public class TaskMove implements ITask {

	Path p = null;
	RunnableFuture<Path> futureP = null;
	
	@Override
	public String getTaskName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTaskPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean tickTask(Object... args) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TaskState getTaskState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTaskStateString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canTaskPerform(IEntity entity) {
		// TODO Auto-generated method stub
		return false;
	}

}
