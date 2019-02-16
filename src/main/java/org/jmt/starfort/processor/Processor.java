package org.jmt.starfort.processor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.jmt.starfort.processor.requests.ProcessingRequest;
import org.jmt.starfort.processor.requests.ReusableProcessingRequest;
import org.jmt.starfort.processor.requests.SuspenableProcessingRequest;

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
	
	/**
	 * Total processing operations completed by the processor since init() was called
	 * @return Total processing ops
	 */
	public static long getTotalTicks() {
		return totalTicks;
	}

	/**
	 * Idle (cycles without any task to perform) processing operations completed by the processor since init() was called
	 * @return Idle processing ops
	 */
	public static long getIdleTicks() {
		return idleTicks;
	}

	static boolean online = false;
	
	static volatile AtomicInteger curMoving = new AtomicInteger();
	
	static Deque <ProcessingRequest> proccessingJobs = new ConcurrentLinkedDeque <ProcessingRequest>();
	static Deque <Runnable> simpleJobs = new ConcurrentLinkedDeque <Runnable>();
	
	static ArrayList<Thread> cores = new ArrayList<Thread>();
	
	/**
	 * Number of threads to run
	 */
	static int size = 1;
	
	/**
	 * Initialises and starts the processor
	 */
	public static void init() {
		// Tells threads that they should run
		if (!online) {
			totalTicks = idleTicks = 0;
			cores.clear();
			online = true;

			for (int i = size; i > 0; i--) {
				cores.add(new Thread(new Runnable() {

					@Override
					public void run() {						//Main Processing Core operation definiton
						while (online) {					//Check the processor is up
							totalTicks++;					//Increment tick count for satatistics
							if (simpleJobs.size() > 0) { 	//Code for handling normal runables
								Runnable job = null;		//GET norm runable job
								try {
									job = simpleJobs.pollFirst();
								} catch (NoSuchElementException nsee) {
									//nsee.printStackTrace();
									//Happens when task is being cycled; shouldn't be a problem
								}
								if (job != null) {			//Check if we aquired a task
									Thread.currentThread().setPriority(Thread.currentThread().getPriority()+1);
									job.run(); 				//Process normal runable job
									Thread.currentThread().setPriority(Thread.currentThread().getPriority()-1);
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
										Thread.yield(); // Yield to working threads though
									} 
								}
								try {
									if (first instanceof SuspenableProcessingRequest && ((SuspenableProcessingRequest) first).suspended()) {
										//Job is currently suspended and needs to be pushed to the end of the stack
										curMoving.incrementAndGet();
										ProcessingRequest job = null;
										while (job == null && online) {
											try {
												job = proccessingJobs.pop();
											} catch (NoSuchElementException nsee) {
												//nsee.printStackTrace();
												//Happens when task is being cycled; shouldn't be a problem
											} }

										proccessingJobs.addLast(job);
										curMoving.decrementAndGet();
										continue;
									}
									if (first.complete()) {
										if (first instanceof ReusableProcessingRequest<?> && ((ReusableProcessingRequest<?>)first).autoRepeat()) {
											curMoving.incrementAndGet();
											ProcessingRequest job = null;
											while (job == null && online) {
												try {
													job = proccessingJobs.pop();
												} catch (NoSuchElementException nsee) {
													//nsee.printStackTrace();
													//Happens when task is being cycled; shouldn't be a problem
												} 
											}
											if (job == first) {
												((ReusableProcessingRequest<?>)job).reset();
											}
											proccessingJobs.addLast(job);
											curMoving.decrementAndGet();
										} else {
											proccessingJobs.remove(first);
											//proccessingJobs.remove(0);
										}
									} else {
									try {
									if (first.remaining() > 0) {
										Thread.currentThread().setPriority(Thread.currentThread().getPriority()+1);
										if (!first.processNext()) { idleTicks++; }
										Thread.currentThread().setPriority(Thread.currentThread().getPriority()-1);
									}
									} catch (NoSuchElementException nsee) {
										//nsee.printStackTrace();
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
								//Ensures CPU doesn't drop below 1% idle due to sleeping 
								//as that would be bad and cause lag
								if ((0.0+idleTicks)/totalTicks > 0.01) {
									try {
										Thread.sleep(0, 100);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
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
	
	/**
	 * Shuts down the processor gracefully, waiting for all threads to terminate
	 */
	public static void down() {
		online = false; // All threads loop on this variable; so if this is false they stop looping
	}
	
	/**
	 * Adds request r to the processing pool
	 * @param r A request in the form of a Processing Request
	 */
	public static void addRequest(ProcessingRequest r) {
		proccessingJobs.add(r);
	}
	
	/**
	 * Adds request r to the processing pool
	 * @param r A request in the form of a Runnable
	 */
	public static void addRequest(Runnable r) {
		simpleJobs.add(r);
	}
	
}
