package org.jmt.starfort.event.ui;

import org.jmt.starfort.event.IEventUI;
import org.jmt.starfort.ui.gui.NuklearUtil;
import org.lwjgl.glfw.GLFW;

/**
 * Base GLFW event
 * 
 * Occurs when a user presses a resolvable character on their keyboard.
 * This just translates that into standard EventBus compatible events
 * 
 * @see GLFW#glfwSetCharCallback(long, org.lwjgl.glfw.GLFWCharCallbackI)
 * @see NuklearUtil#nk_jmt_bus_init(org.jmt.starfort.ui.gui.NkCtxGLFW3, long)
 * 
 * @author jediminer543
 *
 */
public class EventChar implements IEventUI {

	boolean consumed = false;
	
	long window;
	int codepoint;
	
	public EventChar(long window, int codepoint) {
		this.window = window;
		this.codepoint = codepoint;
	}

	@Override
	public boolean getEventConsumed() {
		return consumed;
	}

	@Override
	public void consumeEvent() {
		consumed = true;
	}

	@Override
	public long getEventWindow() {
		return window;
	}

	public int getEventCodepoint() {
		return codepoint;
	}
}
