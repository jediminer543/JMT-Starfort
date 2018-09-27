package org.jmt.starfort.test.processor;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.jmt.starfort.processor.Processor;
import org.jmt.starfort.processor.requests.MassProccessRequest;
import org.jmt.starfort.processor.requests.ProcessingRequest;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This test case is borkened, so it is currently disabled
 * 
 * @author jediminer543
 *
 */
public class ProcessorTest {
	
	int var = 0;
	int complete = 0;
	
	//@Test
	public void simpleProcessingTest() {
		var = 0;
		complete = 0;
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				var = 10;
				complete = 1;
			}
		};
		Processor.init();
		Processor.addRequest(r);
		while (complete < 1) {
			
		}
		assertEquals("Processor Runnable Test", var, 10);
		Processor.down();
	}
	
	//@Test
	public void massProcessingTest() {
		var = 0;
		complete = 0;
		ArrayList<Runnable> runnables = new ArrayList<Runnable>();
		runnables.add(new Runnable() {
			@Override
			public void run() {
				var += 20;
				complete++;
			}
		});
		runnables.add(new Runnable() {
			@Override
			public void run() {
				var += 30;
				complete++;
			}
		});
		runnables.add(new Runnable() {
			@Override
			public void run() {
				var += 40;
				complete++;
			}
		});
		Processor.init();
		MassProccessRequest r = new MassProccessRequest(runnables);
		Processor.addRequest((ProcessingRequest) r);
		while (complete < 3) {
			
		}
		assertEquals("Processor Runnable Test", 90, var);
		Processor.down();
	}

	

}
