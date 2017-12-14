package org.jmt.starfort.processor.requests;

import java.util.ArrayList;
import java.util.Arrays;

import org.jmt.starfort.processor.ComplexRunnable;

/**
 * Executes a set of ComplexRunnables with a common set of arguments
 * 
 * Needs to be manually cycled.
 * 
 * @author Jediminer543
 *
 */
public class ComplexMassProccessRequest implements Runnable, ReusableProcessingRequest<ComplexRunnable> {

	ArrayList<ComplexRunnable> runnablesCurr;
	ArrayList<ComplexRunnable> runnablesNext = new ArrayList<>();
	ArrayList<ComplexRunnable> toRemove = new ArrayList<>();
	ArrayList<Object> args;
	
	public ComplexMassProccessRequest(ArrayList<ComplexRunnable> runnables, Object... args) {
		runnablesCurr = runnables;
		this.args = new ArrayList<>(Arrays.asList(args)); 
	}
	
	public void run() {
		processNext();
	}
	
	@Override
	public boolean processNext() {
		if (complete()) {
			return false;
		}
		ComplexRunnable job = null;
		synchronized (runnablesCurr) {
			job = runnablesCurr.remove(0);
			runnablesNext.add(job);
		}
		job.run(args);
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
	public void addCurr(ComplexRunnable item) {
		synchronized (runnablesCurr) {
			runnablesCurr.add(item);
		}
	}

	@Override
	public void addNext(ComplexRunnable item) {
		synchronized (runnablesNext) {
			runnablesNext.add(item);
		}
	}

	@Override
	public boolean contains(ComplexRunnable item) {
		return runnablesCurr.contains(item) || runnablesNext.contains(item);
	}

	@Override
	public void remove(ComplexRunnable item) {
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
