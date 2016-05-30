package org.jmt.starfort.processor.requests;

import java.util.ArrayList;

/**
 * Executes a set of ComplexRunnables with a common set of arguments
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
	public void processNext() {
		if (complete()) {
			return;
		}
		Runnable job = null;
		synchronized (runnablesCurr) {
			job = runnablesCurr.remove(0);
			runnablesNext.add(job);
		}
		job.run();
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
	
	
	
	
}
