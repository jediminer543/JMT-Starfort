package org.jmt.starfort.world;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.jmt.starfort.event.EventBus;
import org.jmt.starfort.event.IEvent;
import org.jmt.starfort.event.EventBus.EventCallback;
import org.jmt.starfort.event.world.EventMove;
import org.jmt.starfort.logging.Logger;
import org.jmt.starfort.processor.ComplexRunnable;
import org.jmt.starfort.processor.requests.ReusableProcessingRequest;
import org.jmt.starfort.processor.requests.SuspenableProcessingRequest;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.component.IComponentTickable;

/**
 * Processes worlds
 * 
 * Only for real time running
 * 
 * @author Jediminer543
 *
 */
public class TickRequest implements ReusableProcessingRequest<Entry<Coord, ArrayList<ComplexRunnable>>>, SuspenableProcessingRequest {

	/**
	 * The number of loops executed
	 */
	int loopCount = 0;

	/**
	 * After how many loops should this request reload the tick map?
	 */
	int reload = 20;

	/**
	 * Reference to world which this tick request is based in
	 */
	World w;

	/**
	 * The target tickrate of a world in TPS
	 */
	float targetTickRate = 20;
	
	/**
	 * Time until rerun
	 * IN NANOS (IS IMPORTANT)
	 */
	long sleepTime = (long) (1000000000/targetTickRate);
	//long sleepTime = 0;

	/**
	 * Time sleep started
	 */
	long sleepStart = System.nanoTime();

	/**
	 * Last time process was run
	 * Used to stop locking
	 */
	long lastProc = System.nanoTime();

	/**
	 * Current ticks to process
	 */
	volatile ConcurrentHashMap <Coord, ArrayList<ComplexRunnable>> ticksCurr= new ConcurrentHashMap <Coord, ArrayList<ComplexRunnable>>();;

	/**
	 * Ticks to process next cycle
	 */
	volatile ConcurrentHashMap <Coord, ArrayList<ComplexRunnable>> ticksNext = new ConcurrentHashMap <Coord, ArrayList<ComplexRunnable>>();

	/**
	 * Ticks to being processed
	 */
	volatile ConcurrentHashMap <ComplexRunnable, Coord> ticksProc = new ConcurrentHashMap <ComplexRunnable, Coord>();


	AtomicInteger runningCount = new AtomicInteger(0);
	
	public double TPS = 0;

	public TickRequest(World w) {
		this.w = w;
		w.tickrequest = this;
		ticksCurr = (ConcurrentHashMap<Coord, ArrayList<ComplexRunnable>>) w.getTicks();
	}

	{
		EventBus.registerEventCallback(new EventCallback() {

			@Override
			public void handleEvent(IEvent ev) {
				if (ev instanceof EventMove && ((EventMove)ev).w == w && ((EventMove)ev).icomp instanceof IComponentTickable) {
					if(!move(((EventMove)ev).src, ((EventMove)ev).dst, ((IComponentTickable)((EventMove)ev).icomp).getTick())) {
						Logger.error("Failed to move component " + ((EventMove)ev).icomp.getComponentName(), "TickRequest");
						Logger.error("Likley Dev error; Check if Tick is changing per getTick() re-call", "TickRequest");
					}
				}
			}

			@SuppressWarnings("unchecked") // Cant fix because reasons
			@Override
			public Class<? extends IEvent>[] getProcessableEvents() {
				return new Class[] {EventMove.class};
			}

			@Override
			public int getPriority() {
				return 0;
			}
		});
	}

	public boolean move(Coord src, Coord dst, ComplexRunnable tgt) {
		try {
			if (ticksCurr.get(src) != null && ticksCurr.get(src).contains(tgt)) {
				ticksCurr.get(src).remove(tgt);
				ticksCurr.get(dst).add(tgt);
				return true;
			}
		} catch (NullPointerException npe) {/*SyncException*/}
		try {
			if (ticksNext.get(src) != null && ticksNext.get(src).contains(tgt)) {
				ticksNext.get(src).remove(tgt);
				ticksNext.get(dst).add(tgt);
				return true;
			}
		} catch (NullPointerException npe) {/*SyncException*/}
		if (ticksProc.get(tgt) != null && ticksProc.get(tgt).equals(src)) {
			ticksProc.put(tgt, dst);
			return true;
		}
		return false;
	}

	@Override
	public boolean processNext() {
		//Setup Variables
		boolean bumpedRunning = false;
		lastProc = System.nanoTime();
		ComplexRunnable task = null;
		Coord execLoc = null;
		//Aquire task to process
		while (task == null) {
			try {
				if (ticksCurr.entrySet().size() == 0) {
					//If there is nothing to process, stop processing
					return false;
				}
				//Get First entry
				Entry<Coord, ArrayList<ComplexRunnable>> item = ticksCurr.entrySet().iterator().next();
				synchronized (item.getValue()) {
					if (item.getValue().size() > 0) {
						if (bumpedRunning) {
							System.out.println("ERR");
						} else {
							runningCount.incrementAndGet(); bumpedRunning = true;
						}
						task = item.getValue().remove(0);
						execLoc = item.getKey();
					} else {
						synchronized (ticksCurr) {
							ticksCurr.remove(item.getKey());
						}
					}
				}
			} catch (NoSuchElementException nsee) {
				//SYNC ERROR IGNORING
				return false;
			} finally {
				if (bumpedRunning) {
					runningCount.decrementAndGet();
				}
			}
		}
		synchronized (ticksProc) {
			ticksProc.put(task, execLoc);
		}

		//Execute task, making certain that it doesn't kill the thread
		try {
			task.run(w, execLoc, this);
		} catch (Exception e) {
			System.err.println("ERROR: " + task.getClass().getName() + " threw an exception to the processing thread");
			e.printStackTrace();
		}

		boolean complete = false;
		while (!complete) {
			try {
				if (ticksNext.get(ticksProc.get(task)) == null) {
					ArrayList<ComplexRunnable> dsta = new ArrayList<ComplexRunnable>();
					dsta.add(task);
					ticksNext.put(ticksProc.get(task), dsta);
					complete = true;
				} else {
					ticksNext.get(ticksProc.get(task)).add(task);
					complete = true;
				}
			} catch (NullPointerException npe) {/*concurrency error*/ }
		}
		if (!bumpedRunning) {
			//Logs when running count is not increased, as this could cause premature resetting
			System.out.println("ERR");
		}
		runningCount.decrementAndGet();
		return true;
	}

	//TEST PLEASE IGNORE private volatile int completeCheckCount = 0;
	
	@Override
	public boolean complete() {
		//if (!wasDone && (sleepTime + sleepStart <= System.nanoTime())) {
		if (runningCount.get() < 0) {
			runningCount.set(0);
			//System.out.println("ERROR: running count lt 0");
		}
		/* TEST PLEASE IGNORE
		if (completeCheckCount >= 10) {
			completeCheckCount = 0;
			if (runningCount.get() != ticksProc.size()) {
				System.out.println("ERROR: running count desync, fixing");
				runningCount.set(ticksProc.size());
			}
		}
		completeCheckCount++;
		 */
		//if (suspended()) {
		//	return true;
		//}
		return (remaining() == 0 && runningCount.get() == 0);
		//} else {
		//	return true;
		//}
	}

	@Override
	public void reset() {
		if (!complete()) {
			Logger.trace("TickRequest Reset when not Complete");
			return;
		}
		try {
			long endTime = System.nanoTime();
			long frameTime = endTime - sleepStart;
			if (!Double.isFinite(TPS)) {
				TPS=0;
			}
			TPS = (TPS*loopCount+(1000000000.0/frameTime))/(loopCount+1);
			/*
			if (loopCount % 10 == 0) {
				System.out.println("TPS: " + TPS);
			}
			*/
			float tol = 0.001f;
			if (TPS < targetTickRate-tol) {
				sleepTime -= (long)(100000.0*Math.abs(TPS-targetTickRate));
			} else if (TPS > targetTickRate+tol) {
				sleepTime += (long)(100000.0*Math.abs(TPS-targetTickRate));
			}
			//Occasionally throws Div zero exception
		} catch (ArithmeticException e) {}
		//glRotatef(-90, 0, 0, 1);
		loopCount++;
		if (!(loopCount % reload == 0)) {
			synchronized (ticksCurr) {
				ticksCurr = ticksNext;
			}
		} else {
			synchronized (ticksCurr) {
				ticksCurr = (ConcurrentHashMap<Coord, ArrayList<ComplexRunnable>>) w.getTicks();
			}
		}
		if (lastProc + sleepTime > System.nanoTime())
			sleepStart = System.nanoTime();
		synchronized (ticksNext) {
			ticksNext = new ConcurrentHashMap <Coord, ArrayList<ComplexRunnable>>();
		}
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
		int count = 0;
		for (Entry<Coord, ArrayList<ComplexRunnable>> entry : ticksCurr.entrySet()) {
			count += entry.getValue().size();
		}
		return count;
		//No tasks to process effectively

		//Source: http://stackoverflow.com/questions/5496944/java-count-the-total-number-of-items-in-a-hashmapstring-arrayliststring
		// For science; since this method has been being slow
		//return ticksCurr.values().stream().mapToInt(List::size).sum();
	}

	@Override
	public boolean autoRepeat() {
		return true;
	}

	@Override
	public boolean suspended() {
		return (sleepTime + sleepStart > System.nanoTime());
		//return false;
	}

}
