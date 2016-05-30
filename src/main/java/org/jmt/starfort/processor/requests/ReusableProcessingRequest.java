package org.jmt.starfort.processor.requests;

/**
 * Defines a reusable processing request
 * 
 * @author Jediminer543
 *
 * @param <T> 
 */
public interface ReusableProcessingRequest<T> extends ProcessingRequest {
	
	/**
	 * Resets the job so that it can be reused
	 */
	public void reset();
	
	/**
	 * Adds a processing request to this cycle
	 * 
	 * @param item The request to add
	 */
	public void addCurr(T item);
	
	/**
	 * Adds a processing request to the next cycle
	 * 
	 * @param item The request to add
	 */
	public void addNext(T item);
	
	/**
	 * Checks if que contains processed item request
	 * 
	 * @param item
	 */
	public boolean contains(T item);
	
	/**
	 * Remove the item from the process que
	 * 
	 * @param item
	 */
	public void remove(T item);
	

}
