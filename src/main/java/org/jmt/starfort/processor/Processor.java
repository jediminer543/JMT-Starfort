package org.jmt.starfort.processor;

import java.util.ArrayList;

import org.jmt.starfort.processor.requests.ProcessingRequest;

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
	
	static ArrayList<ProcessingRequest> proccessingJobs = new ArrayList<ProcessingRequest>();
	static ArrayList<Runnable> simpleJobs = new ArrayList<Runnable>();
	
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
								simpleJobs.remove(0).run();
							}
							else if (proccessingJobs.size() > 0) {
								if (proccessingJobs.get(0).complete()) {
									proccessingJobs.remove(0);
								} else {
									proccessingJobs.get(0).processNext();
								}
							} else {
								idleTicks++;
							}
						}
					}

				}, "Processor-Core-" + i));
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
