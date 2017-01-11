package org.jmt.starfort.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {
	
	public static interface EventCallback {
		
		public Class<? extends IEvent>[] getProcessableEvents();
		
		public void handleEvent(IEvent ev);
	}

	static Map<Class<? extends IEvent>, ArrayList<EventCallback>> callbacks = new HashMap<Class<? extends IEvent>, ArrayList<EventCallback>>();
	
	public static void registerEventCallback(EventCallback ec) {
		for (Class<? extends IEvent> ev : ec.getProcessableEvents()) {
			if (!callbacks.containsKey(ev)) {
				callbacks.put(ev, new ArrayList<EventCallback>());
			}
			callbacks.get(ev).add(ec);
		}
	}
	
	public static void unregisterEventCallback(EventCallback ec) {
		for (Class<? extends IEvent> ev : ec.getProcessableEvents()) {
			if (callbacks.containsKey(ev)) {
				callbacks.get(ev).remove(ec);
			}
		}
	}
	
	public static void fireEvent(IEvent ev) {
		if (callbacks.containsKey(ev.getClass())) {
			if (callbacks.get(ev.getClass()) == null) {
				callbacks.put(ev.getClass(), new ArrayList<EventBus.EventCallback>());
			} else {
				for (EventCallback ec : callbacks.get(ev.getClass())) {
					ec.handleEvent(ev);
				}
			}
		}
	}
}
