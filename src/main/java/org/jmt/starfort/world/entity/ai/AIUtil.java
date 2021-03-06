package org.jmt.starfort.world.entity.ai;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jmt.starfort.game.entity.human.EntityHuman;
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

	/**
	 * Movement state for entity movement. This is supposed 
	 * to encapsulate all movement capacity of an AI.
	 * 
	 * Also allows for future expansion once node-based pathing is implemented
	 * 
	 * @author jediminer543
	 *
	 */
	public static class MoveState implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -6455082014272717617L;
		
		/**
		 * Initialises move state, and determines
		 * how long entity must wait for (when in)
		 */
		public MoveState() {};
		
		/**
		 * Wait time as calced by initializer
		 */
		public int maxWait = 5; 
		public int wait = 0;
		public Future<Path> fp;
		public Path p;
	}
	
	public static boolean controledEntityMoveTo(World w, Coord src, IEntity ie, Coord dest, MoveState ms) throws CannotPathException {
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
				if (ms.p == null) {
					throw new CannotPathException("Pathing failed");
				}
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
			if(ms.wait <= 0) {
				//Not Waiting
				Direction dir = ms.p.pop();
				if (ie.getEntityAI().getEntityAIPassageCallback().canPass(w, src, dir)) {
					//Path is still valid; can move
					Coord dst = src.add(dir.getDir());
					w.moveComponent(ie, src, dst);
					ms.wait = ms.maxWait;
				} else  {
					//Path is invalid, reset and recalc;
					ms.p = null;
				}
			} else {
				//Decrement wait timer
				if (ms.wait > 0) ms.wait--;
			}
		}
		return false;
	}
	
	public static boolean controledEntityMoveToRange(World w, Coord src, IEntity ie, Coord dest, MoveState ms, Coord range) throws CannotPathException {
		//State 1; At dest
		if (inRangeBetween(src, dest, range)) {
			return true;
		}
		//State 2: Need Path Generator
		if (ms.p == null && ms.fp == null) {
			ms.fp = BruteforcePather.pathBetweenRangeAsync(src, dest, range, w, ie.getEntityAI().getEntityAIPassageCallback());
			Processor.addRequest((Runnable) ms.fp);
			ms.wait=0;
		}
		//State 3: Path Generated
		if (ms.fp != null && ms.fp.isDone()) {
			try {
				ms.p = ms.fp.get(100, TimeUnit.MICROSECONDS);
				ms.fp = null;
				if (ms.p == null) {
					throw new CannotPathException("Pathing failed");
				}
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
			ms.fp = BruteforcePather.pathBetweenRangeAsync(src, dest, range, w, ie.getEntityAI().getEntityAIPassageCallback());
			Processor.addRequest((Runnable) ms.fp);
			ms.p = null;
		} else if (ms.p != null && ms.p.remaining() > 0) {
			//State 4.2 Path Running
			if(ms.wait <= 0) {
				//Not Waiting
				Direction dir = ms.p.pop();
				if (ie.getEntityAI().getEntityAIPassageCallback().canPass(w, src, dir)) {
					//Path is still valid; can move
					Coord dst = src.add(dir.getDir());
					w.moveComponent(ie, src, dst);
					ms.wait = ms.maxWait;
				} else  {
					//Path is invalid, reset and recalc;
					ms.p = null;
				}
			} else {
				//Decrement wait timer
				if (ms.wait > 0) ms.wait--;
			}
		}
		return false;
	}
	
	/**
	 * Unified standard range for interact range calculations
	 * 
	 * @param src Source position (Position of acting entity)
	 * @param dest 
	 * @return
	 */
	public static int rangeBetween(Coord src, Coord dest) {
		return dest.sub(src).abs().manhattan();
	}
	
	/**
	 * Unified standard range for interact range calculations
	 * 
	 * @param src Source position (Position of acting entity)
	 * @param dest 
	 * @return
	 */
	public static boolean inRangeBetween(Coord src, Coord dest, Coord range) {
		Coord thing = dest.sub(src).abs();
		return thing.max(range).equals(range);
	}
}
