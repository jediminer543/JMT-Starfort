package org.jmt.starfort.world.entity.aiold;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;
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
	
	public Task(String name, int priority, ITask[] subtasks) {
		this.name = name;
		this.priority = priority;
		this.subtasks = new ArrayDeque<ITask>(Arrays.asList(subtasks)); 
	}
	
	@Override
	public String getTaskName() {
		return name;
	}

	@Override
	public int getTaskPriority() {
		return priority;
	}

	@Override
	public boolean tickTask(World w, IEntity e, Coord c, Object... args) {
		if (subtasks.getFirst().getTaskState() == TaskState.COMPLETE) {
			subtasks.pop();
		}
		if (subtasks.getFirst().getTaskState() != TaskState.WAITING &&
				subtasks.getFirst().getTaskState() != TaskState.COMPLETE) {
			subtasks.getFirst().tickTask(w, e, c, this, args);
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
