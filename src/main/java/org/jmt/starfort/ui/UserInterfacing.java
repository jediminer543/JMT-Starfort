package org.jmt.starfort.ui;

import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import org.jmt.starfort.event.EventBus;
import org.jmt.starfort.event.events.ui.EventChar;
import org.jmt.starfort.event.events.ui.EventCursorPos;
import org.jmt.starfort.event.events.ui.EventKey;
import org.jmt.starfort.event.events.ui.EventMouseButton;
import org.jmt.starfort.event.events.ui.EventScroll;

public class UserInterfacing {

	public static void setupInterfacing(long win) {
		glfwSetScrollCallback(win, (window, xoffset, yoffset) -> EventBus.fireEvent(new EventScroll(window, xoffset, yoffset)));
		glfwSetCharCallback(win, (window, codepoint) -> EventBus.fireEvent(new EventChar(window, codepoint)));
		glfwSetKeyCallback(win, (window, key, scancode, action, mods) -> EventBus.fireEvent(new EventKey(window, key, scancode, action, mods)));
		glfwSetCursorPosCallback(win, (window, xpos, ypos) -> EventBus.fireEvent(new EventCursorPos(window, xpos, ypos)));
		glfwSetMouseButtonCallback(win, (window, button, action, mods) -> EventBus.fireEvent(new EventMouseButton(window, button, action, mods)));
	}
	
	public static void clickToWorld() {
		
	}
}
