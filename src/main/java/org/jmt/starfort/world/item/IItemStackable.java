package org.jmt.starfort.world.item;

/**
 * 
 * Flags an item as stackable, thus indicating that it has no essential
 * state that will not survive a fork/merge of stack.
 * 
 * 
 * 
 * @author jediminer543
 *
 */
public interface IItemStackable extends IItem, Cloneable {

	public default boolean canItemMerge(IItem iis) {
		return (this.getClass().isInstance(iis));
	}
	
	/**
	 * Merges any parameters of into this object (as one can assume this 
	 * will be called upon the template of a given stack)
	 * 
	 * One should attempt to average out any statistical parameters; I.e. decay
	 * 
	 * @param iis
	 * @param count the number of items already in the stack
	 */
	public default void doItemMerge(IItem iis, int count) {
		//Do Nothing
	}
	
	/**
	 * Create a new instance of this item, to function as if it has been removed from the stack
	 * 
	 * Should typically be handled by java default cloning, but can be overridden
	 */
	public IItemStackable doItemFork();
}
