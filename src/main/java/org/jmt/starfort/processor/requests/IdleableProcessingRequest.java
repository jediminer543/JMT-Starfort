package org.jmt.starfort.processor.requests;

public interface IdleableProcessingRequest extends ProcessingRequest {

	/**
	 * 
	 * 
	 * @return
	 */
	public boolean isRequestIdle();
}
