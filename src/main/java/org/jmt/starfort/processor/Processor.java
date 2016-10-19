package org.jmt.starfort.processor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;

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
	
	static boolean online = false;
	
	static LinkedBlockingDeque<ProcessingRequest> proccessingJobs = new LinkedBlockingDeque<ProcessingRequest>();
	static LinkedBlockingDeque<Runnable> simpleJobs = new LinkedBlockingDeque<Runnable>();
	
	static ArrayList<Thread> cores = new ArrayList<Thread>();
	
	/**
	 * Number of threads to run
	 */
	static int size = 64;
	
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
								simpleJobs.pop().run();
							}
							else if (proccessingJobs.size() > 0) {
								if (proccessingJobs.getFirst().complete()) {
									if (proccessingJobs.getFirst() instanceof ReusableProcessingRequest<?>) {
										((ReusableProcessingRequest<?>) proccessingJobs.getFirst()).reset();
										proccessingJobs.addLast(proccessingJobs.pop());
									} else {
										proccessingJobs.remove(0);
									}
								} else {
									if (proccessingJobs.getFirst().remaining() > 0) {
										proccessingJobs.getFirst().processNext();
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
