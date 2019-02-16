package org.jmt.starfort.world.entity.ai;

public class CannotPathException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6999442179697848820L;

	public CannotPathException(String mesg) {
		super(mesg);
	}
	
	public CannotPathException(String mesg, Throwable e) {
		super(mesg, e);
	}
	
	public CannotPathException(Throwable e) {
		super(e);
	}
	
	public CannotPathException() {
		super();
	}
}
