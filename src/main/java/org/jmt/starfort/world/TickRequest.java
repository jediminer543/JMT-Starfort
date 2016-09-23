package org.jmt.starfort.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.processor.requests.ReusableProcessingRequest;
import org.jmt.starfort.util.Coord;

/**
 * Processes worlds
 * 
 * Only for real time running
 * 
 * @author Jediminer543
 *
 */
public class TickRequest implements ReusableProcessingRequest<Entry<Coord, ArrayList<ComplexRunnable>>> {

	/**
	 * The number of loops executed
	 */
	int loopCount = 0;
	
	/**
	 * After how many loops should this request reload the tick map?
	 */
	int reload = 100;
	
	/**
	 * Reference to world which this tick request is based in
	 */
	World w;
	
	/**
	 * Current ticks to process
	 */
	volatile Map<Coord, ArrayList<ComplexRunnable>> ticksCurr;
	
	/**
	 * Ticks to process next cycle
	 */
	volatile Map<Coord, ArrayList<ComplexRunnable>> ticksNext = new HashMap<Coord, ArrayList<ComplexRunnable>>();
	
	AtomicInteger runningCount = new AtomicInteger(0);
	
	public TickRequest(World w) {
		this.w = w;
		ticksCurr = w.getTicks();
	}
	
	public boolean move(Coord src, Coord dst, ComplexRunnable tgt) {
		if (ticksCurr.get(src).contains(tgt)) {
			ticksCurr.get(src).remove(tgt);
			ticksCurr.get(dst).add(tgt);
			return true;
		}
		if (ticksNext.get(src).contains(tgt)) {
			ticksNext.get(src).remove(tgt);
			ticksNext.get(dst).add(tgt);
			return true;
		}
		return false;
	}
	
	@Override
	public void processNext() {
		ComplexRunnable task = null;
		Coord execLoc = null;
		while (task == null) {
			if (ticksCurr.entrySet().size() == 0) {
				return;
			}
			Entry<Coord, ArrayList<ComplexRunnable>> item = ticksCurr.entrySet().iterator().next();
			if (item.getValue().size() > 0) {
				task = item.getValue().remove(0);
				execLoc = item.getKey();
			} else {
				synchronized (ticksCurr) {
					ticksCurr.remove(item.getKey());
				}
			}
		}
		runningCount.incrementAndGet();
		task.run(w, execLoc, this);
		runningCount.decrementAndGet();
		synchronized (ticksNext) {
			ticksNext.get(execLoc).add(task);
		}
		
	}

	@Override
	public boolean complete() {
		return remaining() == 0 && runningCount.get() == 0;
	}

	@Override
	public void reset() {
		loopCount++;
		if (!(loopCount % reload == 0)) {
			ticksCurr = ticksNext;
		} else {
			ticksCurr = w.getTicks();
		}
		ticksNext = new HashMap<Coord, ArrayList<ComplexRunnable>>();
	}

	@Override
	public void addCurr(Entry<Coord, ArrayList<ComplexRunnable>> item) {
		synchronized (ticksCurr) {
			ticksCurr.entrySet().add(item);
		}
	}

	@Override
	public void addNext(Entry<Coord, ArrayList<ComplexRunnable>> item) {
		synchronized (ticksNext) {
			ticksNext.entrySet().add(item);
		}
	}

	@Override
	public boolean contains(Entry<Coord, ArrayList<ComplexRunnable>> item) {
		return ticksCurr.entrySet().contains(item) || ticksNext.entrySet().contains(item);
	}

	@Override
	public void remove(Entry<Coord, ArrayList<ComplexRunnable>> item) {
		synchronized (ticksCurr) {
			ticksCurr.entrySet().remove(item);
		}
		synchronized (ticksNext) {
			ticksNext.entrySet().remove(item);
		}
	}

	@Override
	public int remaining() {
		synchronized (ticksCurr) {
			int count = 0;
			for (Entry<Coord, ArrayList<ComplexRunnable>> entry : ticksCurr.entrySet()) {
				count += entry.getValue().size();
			}
			return count;
		}
	}

}
