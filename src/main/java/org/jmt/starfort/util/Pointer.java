package org.jmt.starfort.util;

/**
 * Used incase C style pointers are needed at any point (da dum tsch)
 * 
 * Allows you to pass a method a pointer, and return a value as part
 * of the method call, a-la C/C++;
 * 
 * May be usefull.
 * 
 * @author Jediminer543
 *
 * @param <T> The type of class this pointer will point to
 */
public class Pointer<T> {
	
	T pointed;
	// Literally useless
	boolean deleted;
	
	public T getPointed() {
		return pointed;
	}
	
	//Sets target of pointer
	public T setPointed(T newPointed) {
		T oldPointed = pointed;
		pointed = newPointed;
		deleted = false;
		return oldPointed;
	}
	
	// Literally does nothing
	public void delete() {
		deleted = true;
	}

}
