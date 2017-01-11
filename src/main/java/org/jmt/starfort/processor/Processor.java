package org.jmt.starfort.processor;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.jmt.starfort.processor.requests.ProcessingRequest;
import org.jmt.starfort.processor.requests.ReusableProcessingRequest;

/**
 * Statically accessible threading pool
 * 
 * Now named BOB
 * 
 * @author Jediminer543
 *
 */
public class Processor {

	static long totalTicks, idleTicks;
	
	public static long getTotalTicks() {
		return totalTicks;
	}

	public static long getIdleTicks() {
		return idleTicks;
	}

	static boolean online = false;
	
	static volatile AtomicInteger curMoving = new AtomicInteger();
	
	static LinkedBlockingDeque <ProcessingRequest> proccessingJobs = new LinkedBlockingDeque <ProcessingRequest>();
	static LinkedBlockingDeque <Runnable> simpleJobs = new LinkedBlockingDeque <Runnable>();
	
	static ArrayList<Thread> cores = new ArrayList<Thread>();
	
	/**
	 * Number of threads to run
	 */
	static int size = 128;
	
	public static void init() {
		// Tells threads that they should run
		if (!online) {
			totalTicks = idleTicks = 0;
			cores.clear();
			online = true;

			for (int i = size; i > 0; i--) {
				cores.add(new Thread(new Runnable() {

					@Override
					public void run() {
						while (online) {
							totalTicks++;
							if (simpleJobs.size() > 0) {
								Runnable job = null;
								try {
									job = simpleJobs.pollFirst(100, TimeUnit.MICROSECONDS);
								} catch (InterruptedException nsee) {
									//nsee.printStackTrace();
									//Happens when task is being cycled; shouldn't be a problem
								}
								if (job != null) {
									job.run();
								}
							}
							else if (proccessingJobs.size() - curMoving.get() > 0) {
								ProcessingRequest first = null;
								while (first == null && online) {
									try {
										first = proccessingJobs.getFirst();
									} catch (NoSuchElementException nsee) {
									//nsee.printStackTrace();
									//Happens when task is being cycled; shouldn't be a problem
								} }
								try {
								if (first.complete()) {
									if (first instanceof ReusableProcessingRequest<?>) {
										curMoving.incrementAndGet();
										ProcessingRequest job = null;
										while (job == null && online) {
										try {
											job = proccessingJobs.pop();
										} catch (NoSuchElementException nsee) {
										//nsee.printStackTrace();
										//Happens when task is being cycled; shouldn't be a problem
										} }
										((ReusableProcessingRequest<?>) job).reset();
										proccessingJobs.addLast(job);
										curMoving.decrementAndGet();
									} else {
										proccessingJobs.remove(first);
										//proccessingJobs.remove(0);
									}
								} else {
									try {
									if (first.remaining() > 0) {
										first.processNext();
									}
									} catch (NoSuchElementException nsee) {
										nsee.printStackTrace();
										//Happens when task is being cycled; shouldn't be a problem
									}
								}
								} catch (NullPointerException npe) {
									//NPE on thread issue; common on shutdown
									if (online) {
										npe.printStackTrace();
									}
								}
							} else {
								idleTicks++;
							}
						}
					}

				}, "Processor-Core-" + i));
			}
			for (Thread t : cores) {
				t.start();
			}
		}
	}
	
	public static void down() {
		online = false;
	}
	
	public static void addRequest(ProcessingRequest r) {
		proccessingJobs.add(r);
	}
	
	public static void addRequest(Runnable r) {
		simpleJobs.add(r);
	}
	
}
