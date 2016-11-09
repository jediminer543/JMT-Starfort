package org.jmt.starfort.world.entity.ai;

import java.util.ArrayDeque;
import java.util.Deque;

import org.jmt.starfort.world.entity.IEntity;

public class Task implements ITask {

	String name;
	int priority;
	Deque<ITask> subtasks = new ArrayDeque<>();
	
	public Task(String name, int priority, Deque<ITask> subtasks) {
		this.name = name;
		this.priority = priority;
		this.subtasks = subtasks; 
	}
	
	@Override
	public String getTaskName() {
		return name;
	}

	@Override
	public int getTaskPriority() {
		return 0;
	}

	@Override
	public boolean tickTask(Object... args) {
		if (subtasks.getFirst().getTaskState() == TaskState.COMPLETE) {
			subtasks.pop();
		}
		if (subtasks.getFirst().getTaskState() != TaskState.WAITING &&
				subtasks.getFirst().getTaskState() != TaskState.COMPLETE) {
			subtasks.getFirst().tickTask(this, args);
			return true;
		}
		return false;
	}

	@Override
	public TaskState getTaskState() {
		return subtasks.size() > 0 ? subtasks.getFirst().getTaskState() : TaskState.COMPLETE;
	}

	@Override
	public String getTaskStateString() {
		return subtasks.size() > 0 ? subtasks.getFirst().getTaskStateString() : "Task Complete";
	}

	@Override
	public boolean canTaskPerform(IEntity entity) {
		return subtasks.size() > 0 && subtasks.getFirst().canTaskPerform(entity);
	}

}
