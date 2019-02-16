package org.jmt.starfort.event;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * A Bus. For events. An Event Bus
 * <br><br>
 * Seriously, if you need to know what an event bus is
 * then you are probably past me.
 * <br>
 * Go and build a massive code base that works but you
 * have no idea how instead of reading this. 
 * <br><br>
 * But Seriously, this stores callbacks, and then passes
 * them events from anything that wants to fire events.
 * Thats what event busses do.
 * 
 * @author jediminer543
 * @see IEvent
 */
public class EventBus {
	
	/**
	 * A callback for event handling.
	 * 
	 * Should probably build a functional interface constructor for these
	 * 
	 * But thats to be added to the TODO list
	 * 
	 * @author jediminer543
	 *
	 */
	public static interface EventCallback {
		
		/**
		 * The classes of events this callback will be run on
		 * 
		 * DOESN'T itterate down the inheritance tree.
		 * 
		 * @return events this callback will process
		 */
		public Class<? extends IEvent>[] getProcessableEvents();
		
		/**
		 * Handle event EV
		 * 
		 * @param ev Event to handle
		 * 
		 * @see IEvent
		 */
		public void handleEvent(IEvent ev);
		
		/**
		 * Priority of this callback. Useful to avoid click pass-through.
		 * 
		 * @return Priority of callback
		 * 
		 * @see IEvent
		 * @see IEvent#consumeEvent()
		 */
		public int getPriority();
	}

	/**
	 * List of callbacks
	 */
	static Map<Class<? extends IEvent>, ArrayList<EventCallback>> callbacks = new HashMap<Class<? extends IEvent>, ArrayList<EventCallback>>();
	
	/**
	 * Register a callback for calling.
	 * 
	 * Also sorts all current callbacks
	 * 
	 * @param ec
	 * 
	 * @see EventCallback
	 */
	public static void registerEventCallback(EventCallback ec) {
		if (ec.getProcessableEvents() != null)
		for (Class<? extends IEvent> ev : ec.getProcessableEvents()) {
			if (!callbacks.containsKey(ev)) {
				callbacks.put(ev, new ArrayList<EventCallback>());
			}
			callbacks.get(ev).add(ec);
			callbacks.get(ev).sort(Comparator.comparingInt(EventCallback::getPriority).reversed());
		}
	}
	
	/**
	 * Remove a current callback
	 * 
	 * Does nothing if callback isn't registered
	 * 
	 * @param ec EventCallback to unregister
	 * 
	 * @see EventCallback
	 */
	public static void unregisterEventCallback(EventCallback ec) {
		for (Class<? extends IEvent> ev : ec.getProcessableEvents()) {
			if (callbacks.containsKey(ev)) {
				callbacks.get(ev).remove(ec);
			}
		}
	}
	
	/**
	 * Fire an event, calling all callback in order of priority, from highest to lowest.
	 * 
	 * While built for standard events, anything implementing IEvent will be usable, if you
	 * want to mess with stuff.s
	 * 
	 * @param ev Event fire
	 * 
	 * @see IEvent
	 * @see EventCallback
	 */
	public static void fireEvent(IEvent ev) {
		if (callbacks.containsKey(ev.getClass())) {
			if (callbacks.get(ev.getClass()) == null) {
				//This should be unreachable right? But it is a safety measure, so why not leave it in
				callbacks.put(ev.getClass(), new ArrayList<EventBus.EventCallback>());
			} else {
				//List is pre-sorted
				for (EventCallback ec : callbacks.get(ev.getClass())) {
					ec.handleEvent(ev);
				}
			}
		}
	}
}