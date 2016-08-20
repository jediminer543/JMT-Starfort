package org.jmt.starfort.util;

/**
 * Used incase C style pointers are needed at any point (da dum tsch)
 * 
 * @author Jediminer543
 *
 * @param <T> The type of class this pointer will point to
 */
public class Pointer<T> {
	
	T pointed;
	boolean deleted;
	
	public T getPointed() {
		return pointed;
	}
	
	public T setPointed(T newPointed) {
		T oldPointed = pointed;
		pointed = newPointed;
		deleted = false;
		return oldPointed;
	}
	
	public void delete() {
		deleted = true;
	}

}
