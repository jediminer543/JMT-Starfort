package org.jmt.starfort.ui.gui.window;

import static org.lwjgl.nuklear.Nuklear.*;

import java.nio.IntBuffer;

import org.jmt.starfort.Starfort;
import org.jmt.starfort.event.EventBus;
import org.jmt.starfort.event.IEvent;
import org.jmt.starfort.event.IEventConsumable;
import org.jmt.starfort.event.ui.EventKey;
import org.jmt.starfort.ui.gui.NkCtxGLFW3;
import org.jmt.starfort.ui.gui.widget.IWidget;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;

public class WindowPause implements IWidget {

	public boolean show = true;
	
	public WindowPause() {
		EventBus.registerEventCallback(new EventBus.EventCallback() {
			
			@Override
			public void handleEvent(IEvent ev) {
				if (ev instanceof IEventConsumable && !((IEventConsumable) ev).getEventConsumed()){
					if (ev instanceof EventKey) {
						EventKey kev = (EventKey)(ev);
						if (kev.getEventAction() == GLFW.GLFW_PRESS) {
							switch (kev.getEventKey()) {
								case (GLFW.GLFW_KEY_ESCAPE):
									show = !show;
									break;
							}
						}
					} 
				}	
			}
			
			@SuppressWarnings("unchecked") // Cant fix because reasons
			@Override
			public Class<? extends IEvent>[] getProcessableEvents() {
				return new Class[] {EventKey.class};
			}
	
			@Override
			public int getPriority() {
				return 0;
			}
			
			
		});
	}
	
	//private IntBuffer[] tabStates = new IntBuffer[2];
	private IntBuffer tabStates = BufferUtils.createIntBuffer(2);
	{
		tabStates.put(1);
	}
	//Initialises all tab states
	//{
	//	tabStates[0] = BufferUtils.createIntBuffer(1);
	//	tabStates[0].put(NK_MAXIMIZED);
	//	tabStates[0].rewind();
	//	tabStates[1] = BufferUtils.createIntBuffer(1);
	//}
	
	private int winWidth = 256, winHeight = 400;
	
	@Override
	public void drawWidget(NkCtxGLFW3 jctx) {
		NkRect bounds = NkRect.mallocStack(jctx.stack);
		NkContext ctx = jctx.ctx;
		if (show) {
			if (nk_begin(ctx, "Pause", nk_rect(Starfort.winWidth/2 - winWidth/2, Starfort.winHeight/4, winWidth, winHeight, bounds), 
					NK_WINDOW_BORDER | NK_WINDOW_MOVABLE | NK_WINDOW_MINIMIZABLE)) {
				//THIS ISNT THE FRACKING STANDARD API
				//TE FRACKING CYLONS ARE HERE, WHERE ARE THE FRACKING PSYKERS
				//END RANT
				//
				//FOR FUTURE REFERENCE
				//IT SEEMS THAT TABSATE must be a persistant IntBuffer, which
				//is initalised to the initial state, and is then updated whenever
				//the function is called.
				//DOES THAT MAKE SENSE
				//THAT IS FOR STATE PUSH
				if (nk_tree_state_push(ctx, NK_TREE_TAB, "Game", (IntBuffer)tabStates.position(0))) {
					nk_button_text(ctx, "New");
					nk_button_text(ctx, "Save");
					nk_button_text(ctx, "Load");
					if (nk_button_text(ctx, "Exit")) {
						GLFW.glfwSetWindowShouldClose(Starfort.window, true);
					}
					nk_tree_state_pop(ctx);
				}
				if (nk_tree_state_push(ctx, NK_TREE_TAB, "Options", (IntBuffer)tabStates.position(1))) {
					nk_button_text(ctx, "Sound");
					nk_button_text(ctx, "Graphics");
					nk_button_text(ctx, "Something");
					nk_button_text(ctx, "HELP ME");
					nk_tree_state_pop(ctx);
				}
				
			}
			nk_end(ctx);
		}
	}

	
}
