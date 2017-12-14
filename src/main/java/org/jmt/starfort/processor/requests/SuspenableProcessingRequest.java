package org.jmt.starfort.processor.requests;

public interface SuspenableProcessingRequest extends ProcessingRequest {

	/**
	 * Declare if this processing request is suspended.
	 * When flagged, this will NOT be reset.
	 * 
	 * @return suspended flag
	 */
	public boolean suspended();
}
