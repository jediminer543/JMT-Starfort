package org.jmt.starfort.world.entity.ai;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jmt.starfort.pathing.bruteforce.BruteforcePather;
import org.jmt.starfort.pathing.bruteforce.Path;
import org.jmt.starfort.processor.Processor;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.util.Direction;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.entity.IEntity;

/**
 * Contains utilities for AI logic, such as range calcs, pre-generated move commands etc.
 * 
 * 
 * 
 * @author jediminer543
 *
 */
public class AIUtil {

	public static class MoveState {
		/**
		 * Initialises move state, and determines
		 * how long entity must wait for (when in)
		 * 
		 * @param ie Entity to calc for.
		 */
		public MoveState(IEntity ie) {};
		
		int maxWait = 0;
		int wait = 0;
		Future<Path> fp;
		Path p;
	}
	
	public static boolean controledEntityMoveTo(World w, Coord src, IEntity ie, Coord dest, MoveState ms) {
		if (ms.wait > 0) ms.wait--;
		//State 1; At dest
		if (src.equals(dest)) {
			return true;
		}
		//State 2: Need Path Generator
		if (ms.p == null && ms.fp == null) {
			ms.fp = BruteforcePather.pathBetweenAsync(src, dest, w, ie.getEntityAI().getEntityAIPassageCallback());
			Processor.addRequest((Runnable) ms.fp);
			ms.wait=0;
		}
		//State 3: Path Generated
		if (ms.fp != null && ms.fp.isDone()) {
			try {
				ms.p = ms.fp.get(100, TimeUnit.MICROSECONDS);
				ms.fp = null;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
			}
		}
		//State 4: Path exists
		if (ms.p != null && ms.p.remaining() <= 0 && ms.fp == null) {
			//State 4.1 Path done
			ms.fp = BruteforcePather.pathBetweenAsync(src, dest, w, ie.getEntityAI().getEntityAIPassageCallback());
			Processor.addRequest((Runnable) ms.fp);
			ms.p = null;
		} else if (ms.p != null && ms.p.remaining() > 0) {
			//State 4.2 Path Running
			Direction dir = ms.p.pop();
			if(ms.wait <= 0) {
				//Not Waiting
				if (ie.getEntityAI().getEntityAIPassageCallback().canPass(w, src, dir)) {
					Coord dst = src.add(dir.getDir());
					w.moveComponent(ie, src, dst);
					ms.wait = ms.maxWait;
				} else  {
					ms.p = null;
				}
			}
		}
		return false;
	}
}
