package org.jmt.starfort.world.entity.aiold.subtask;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jmt.starfort.pathing.bruteforce.BruteforcePather;
import org.jmt.starfort.pathing.bruteforce.Path;
import org.jmt.starfort.processor.Processor;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.entity.IEntity;
import org.jmt.starfort.world.entity.aiold.ITask;

public class TaskMove implements ITask {

	Path p = null;
	RunnableFuture<Path> futurePath = null;
	Coord dst;
	TaskState state = TaskState.PRE;
	
	ArrayList<IEntity> cannotComplete = new ArrayList<IEntity>();
	
	public TaskMove(Coord dst) {
		this.dst = dst;
	}
	
	@Override
	public String getTaskName() {
		return "Moving";
	}

	@Override
	public int getTaskPriority() {
		return 20;
	}

	//Incase the entity is randomly relocated
	private Coord assumedCoord;
	
	@Override
	public boolean tickTask(World w, IEntity ie, Coord c, Object... args) {
		if (assumedCoord != null && !c.equals(assumedCoord)) {
			p = null;
			futurePath = null;
		}
		if (p == null && futurePath == null) {
			futurePath = BruteforcePather.pathBetweenAsync(c, dst, w, ie.getEntityPassageCallback());
			Processor.addRequest(futurePath);
		}
		if (futurePath != null && futurePath.isDone()) {
			try {
				p = futurePath.get(100, TimeUnit.MICROSECONDS);
				futurePath = null;
				if (p == null) {
					//System.out.println("Entity:" + ie.getEntityName() + " cannot complete");
					cannotComplete.add(ie);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
			}
		}
		if (p != null && p.remaining() > 0) {
			Coord dst = c.add(p.pop().getDir());
			w.moveComponent(ie, c, dst);
			assumedCoord = dst;
			/*
			tr.move(c, dst, this);
			w.getBlockNoAdd(c).removeComponent(ie);
			w.getBlock(dst).addComponent(ie);
			*/
		}
		if (c.equals(dst) && p != null && p.remaining() == 0) {
			state = TaskState.COMPLETE;
		}
		if (!c.equals(dst) && p != null && p.remaining() == 0 ) {
			state = TaskState.ERROR;
		}
		return false;
	}

	@Override
	public TaskState getTaskState() {
		return state;
	}

	@Override
	public String getTaskStateString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canTaskPerform(IEntity entity) {
		return !cannotComplete.contains(entity);
	}

}
