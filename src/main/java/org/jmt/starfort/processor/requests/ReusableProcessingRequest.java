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
	
	/**
	 * Whether the request should be autocycled by the processor
	 * <br>
	 * <code>if (remaining() < 0 && return) { reset(); } </code>
	 * 
	 * 
	 * @return True if to automaticly cycled
	 */
	public boolean autoRepeat();
	
	/**
	 * Declare if this processing request is suspended.
	 * When flagged, this will NOT be reset.
	 * 
	 * @return suspended flag
	 */
	public boolean suspended();
}
