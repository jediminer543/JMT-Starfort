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
	boolean complete = false;
	@Test
	public void simpleProcessingTest() {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				var = 10;
				complete = true;
			}
		};
		Processor.addRequest(r);
		while (!complete) {
			
		}
		assertEquals("Processor Runnable Test", var, 10);
	}
	
	//@Test TODO FIX THIS TEST
	public void massProcessingTest() {
		ArrayList<Runnable> runnables = new ArrayList<Runnable>();
		runnables.add(new Runnable() {
			@Override
			public void run() {
				var += 20;
			}
		});
		runnables.add(new Runnable() {
			@Override
			public void run() {
				var += 30;
			}
		});
		runnables.add(new Runnable() {
			@Override
			public void run() {
				var += 40;
			}
		});
		MassProccessRequest r = new MassProccessRequest(runnables);
		Processor.addRequest((ProcessingRequest) r);
		while (!r.complete()) {
			
		}
		assertEquals("Processor Runnable Test", var, 100);
	}

	

}
