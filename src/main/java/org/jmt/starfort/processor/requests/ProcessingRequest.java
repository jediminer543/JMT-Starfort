package org.jmt.starfort.processor.requests;

/**
 * Defines a request for processing
 * 
 * @author Jediminer543
 *
 */
public interface ProcessingRequest {

	/**
	 * Processes the next request
	 * @return 
	 */
	public void processNext();
	
	/**
	 * Checks if the processing request has completed
	 * 
	 * @return Whether the job is completed
	 */
	public boolean complete();
	
	/**
	 * How many subsections of this request remain until the request is finished
	 * 
	 * @return How many more processNext() executions remain until this request is complete
	 */
	public int remaining();
}
