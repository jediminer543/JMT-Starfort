package org.jmt.starfort.event;

/**
 * An event to be dispatched by the Event bus.
 * 
 * Should be extended into specific class instead of using
 * anonymous class, as that will make the event bus useless.
 * 
 * @author jediminer543
 *
 * @see EventBus
 * 
 */
public interface IEvent {
	
	/**
	 * Checks if the event has been flagged as consumed.
	 * Consumed events will still continue being dispatched
	 * as not all callback need to respect this. It should
	 * be respected for most events, but isn't required.
	 * 
	 * Alternatively, events may just always return false
	 * 
	 * @return If the event has already been consumed
	 * @see #consumeEvent()
	 * @see EventBus.EventCallback
	 */
	public boolean getEventConsumed();
	
	/**
	 * Flag the event as consumed. This means that the events
	 * data has been used to do an operation that means the event
	 * shouldn't be used for other functions.<br>
	 * 
	 * The main purpose of this is for UI stuff where you don't
	 * want world interaction occurring behind your UI.
	 * 
	 * @see #getEventConsumed()
	 */
	public void consumeEvent();

}
