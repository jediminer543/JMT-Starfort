package org.jmt.starfort.event.ui;

import org.jmt.starfort.event.IEvent;
import org.jmt.starfort.event.IEventConsumable;
import org.jmt.starfort.ui.gui.NuklearUtil;

/**
 * An extension of IEvent designed for UI processing
 * <br><br>
 * Contains the GLFW window ID, which is useful if you,
 * for some reason think that having many windows is a
 * good idea.
 * 
 * @author jediminer543
 *
 * @see IEvent
 * @see NuklearUtil#nk_jmt_bus_init(NkCtxGLFW3, long)
 */
public interface IEventUI extends IEventConsumable {
	
	/**
	 * The GLFW window in which this event occurred
	 * 
	 * @return GLFW Window ID
	 */
	public long getEventWindow();
}
