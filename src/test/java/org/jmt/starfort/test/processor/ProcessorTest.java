package org.jmt.starfort.test.processor;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.jmt.starfort.processor.Processor;
import org.jmt.starfort.processor.requests.MassProccessRequest;
import org.jmt.starfort.processor.requests.ProcessingRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProcessorTest {
	
	@Before
	public void setUp() throws Exception {
		Processor.init();
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	int var = 0;
	int complete = 0;
	
	@Test
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
		Processor.addRequest(r);
		while (complete < 1) {
			
		}
		assertEquals("Processor Runnable Test", var, 10);
	}
	
	@Test
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
		MassProccessRequest r = new MassProccessRequest(runnables);
		Processor.addRequest((ProcessingRequest) r);
		while (complete < 3) {
			
		}
		assertEquals("Processor Runnable Test", 90, var);
	}

	

}
