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
	 */
	public void processNext();
	
	/**
	 * Checks if the processing request has completed
	 * 
	 * @return Whether the job is completed
	 */
	public boolean complete();
	
}
