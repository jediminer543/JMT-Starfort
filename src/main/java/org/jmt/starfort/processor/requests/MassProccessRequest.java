package org.jmt.starfort.processor.requests;

import java.util.ArrayList;

/**
 * Executes a set of runnable(s) with a common set of arguments
 * 
 * Needs to be manually cycled.
 * 
 * @author Jediminer543
 *
 */
public class MassProccessRequest implements Runnable, ReusableProcessingRequest<Runnable> {

	ArrayList<Runnable> runnablesCurr;
	ArrayList<Runnable> runnablesNext = new ArrayList<>();
	ArrayList<Runnable> toRemove = new ArrayList<>();
	
	public MassProccessRequest(ArrayList<Runnable> runnables) {
		runnablesCurr = runnables;
	}
	
	public void run() {
		processNext();
	}
	
	@Override
	public boolean processNext() {
		if (complete()) {
			return false;
		}
		Runnable job = null;
		while (job == null) {
			try {
				job = runnablesCurr.remove(0);
			} catch (IndexOutOfBoundsException e) { 
				// SYNC ERROR; IGNORING 
			}
		}
		runnablesNext.add(job);
		
		job.run();
		return true;
	}
	
	@Override
	public boolean complete() {
		return runnablesCurr.size() == 0;
	}
	
	@Override
	public void reset() {
		synchronized (runnablesNext) {
			runnablesNext.removeAll(toRemove);
			toRemove.clear();
			runnablesCurr.addAll(runnablesNext);
			runnablesNext.clear();
		}
	}

	@Override
	public void addCurr(Runnable item) {
		synchronized (runnablesCurr) {
			runnablesCurr.add(item);
		}
	}

	@Override
	public void addNext(Runnable item) {
		synchronized (runnablesNext) {
			runnablesNext.add(item);
		}
	}

	@Override
	public boolean contains(Runnable item) {
		return runnablesCurr.contains(item) || runnablesNext.contains(item);
	}

	@Override
	public void remove(Runnable item) {
		toRemove.add(item);
	}
	
	@Override
	public int remaining() {
		return runnablesCurr.size();
	}

	@Override
	public boolean autoRepeat() {
		return false;
	}
	
	
	
}
