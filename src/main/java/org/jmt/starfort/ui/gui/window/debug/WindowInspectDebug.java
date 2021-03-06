package org.jmt.starfort.ui.gui.window.debug;

import static org.lwjgl.nuklear.Nuklear.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.IntBuffer;
import java.util.Arrays;

import org.jmt.starfort.Starfort;
import org.jmt.starfort.event.EventBus;
import org.jmt.starfort.event.IEvent;
import org.jmt.starfort.event.IEventConsumable;
import org.jmt.starfort.event.ui.EventKey;
import org.jmt.starfort.event.world.EventWorldClick;
import org.jmt.starfort.logging.Logger;
import org.jmt.starfort.ui.gui.NkCtxGLFW3;
import org.jmt.starfort.ui.gui.widget.IWidget;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.Block;
import org.jmt.starfort.world.component.IComponent;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;

public class WindowInspectDebug implements IWidget {

	public boolean show = false;
	
	public WindowInspectDebug() {
		EventBus.registerEventCallback(new EventBus.EventCallback() {
			
			@Override
			public void handleEvent(IEvent ev) {
				if (ev instanceof IEventConsumable && !((IEventConsumable) ev).getEventConsumed()) {
					if (ev instanceof EventKey) {
						EventKey kev = (EventKey)(ev);
						if (kev.getEventAction() == GLFW.GLFW_PRESS) {
							switch (kev.getEventKey()) {
								case (GLFW.GLFW_KEY_F12):
									show = !show;
									break;
							}
						}
					} else if (ev instanceof EventWorldClick) {
						EventWorldClick wke = (EventWorldClick)(ev);
						block = wke.getEventWorld().getBlock(wke.getEventCoord());
						curPos = wke.getEventCoord();
						offset = 0;
						//Logger.debug(curPos.toString());
					} 
				}	
			}
			
			@SuppressWarnings("unchecked") // Cant fix because reasons
			@Override
			public Class<? extends IEvent>[] getProcessableEvents() {
				return new Class[] {EventKey.class, EventWorldClick.class};
			}
	
			@Override
			public int getPriority() {
				return 0;
			}
			
			
		});
	}
	
	private IntBuffer[][] tabStates = new IntBuffer[3][3];
	
	//Initialises all tab states
	{
		tabStates[0][0] = BufferUtils.createIntBuffer(1);
		//tabStates[0][0].put(NK_MAXIMIZED);
		//tabStates[0][0].rewind();
		tabStates[0][1] = BufferUtils.createIntBuffer(1);
		tabStates[0][2] = BufferUtils.createIntBuffer(1);
		tabStates[1][0] = BufferUtils.createIntBuffer(1);
		tabStates[1][1] = BufferUtils.createIntBuffer(1);
		tabStates[1][2] = BufferUtils.createIntBuffer(1);
		tabStates[2][0] = BufferUtils.createIntBuffer(1);
		tabStates[2][1] = BufferUtils.createIntBuffer(1);
		tabStates[2][2] = BufferUtils.createIntBuffer(1);
		
	}
	
	public Coord curPos;
	public Block block;
	private int offset = 0;
	
	private int winWidth = 256, winHeight = 400;
	
	@Override
	public void drawWidget(NkCtxGLFW3 jctx) {
		NkRect bounds = NkRect.mallocStack(jctx.stack);
		NkContext ctx = jctx.ctx;
		if (show) {
			if (nk_begin(ctx, "Debug", nk_rect((Starfort.winWidth/2 - winWidth/2)*4/3, Starfort.winHeight/4, winWidth, winHeight, bounds), 
					NK_WINDOW_BORDER | NK_WINDOW_MOVABLE | NK_WINDOW_MINIMIZABLE | NK_WINDOW_SCALABLE)) {
				//See Rant in WindowPause
				try {
					nk_layout_row_dynamic(jctx.ctx, 20, 1);
					nk_label(ctx, curPos.toString(), NK_TEXT_ALIGN_CENTERED);
					nk_layout_row_dynamic(jctx.ctx, 20, 2);
					boolean left = nk_button_label(jctx.ctx, "<");
					if (left) {
						offset = Math.max(offset-1, 0);
					}
					boolean right = nk_button_label(jctx.ctx, ">");
					if (right) {
						offset++;
					}
					if (block != null) {
						if (block.getComponents().size() > offset+0) {
							debugComponent(ctx, 0);
							if (block.getComponents().size() > offset+1) {
								debugComponent(ctx, 1);
								if (block.getComponents().size() > offset+2) {
									debugComponent(ctx, 2);
								}
							}
						}
					}
				} catch (NullPointerException npe) {}
				
			}
			nk_end(ctx);
		}
	}
	
	public void debugComponent(NkContext ctx, int index) {
		IComponent comp = block.getComponent(offset+index);
		if (nk_tree_state_push(ctx, NK_TREE_TAB, comp.getComponentName(), tabStates[index][0])) {
			nk_label(ctx, "Material: " + comp.getComponentMaterial().getMaterialName(), NK_TEXT_ALIGN_CENTERED);
			if (nk_tree_state_push(ctx, NK_TREE_TAB, "Fields", tabStates[index][1])) {
				for (Field f:comp.getClass().getDeclaredFields()) {
					f.setAccessible(true);
					try {
						nk_label(ctx, f.getName()+" "+f.get(comp), NK_TEXT_ALIGN_CENTERED);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				for (Field f:comp.getClass().getFields()) {
					f.setAccessible(true);
					try {
						nk_label(ctx, f.getName()+" "+f.get(comp), NK_TEXT_ALIGN_CENTERED);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				nk_tree_state_pop(ctx);
			}
			if (nk_tree_state_push(ctx, NK_TREE_TAB, "Methods", tabStates[index][2])) {
				for (Method m:comp.getClass().getDeclaredMethods()) {
					if (m.isAnnotationPresent(DebugInspectable.class)) {
						try {
							nk_label(ctx, m.getName() + "() " + m.invoke(comp), NK_TEXT_ALIGN_CENTERED);
						} catch (IllegalArgumentException e) {
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					} else {
						try {
							nk_label(ctx, m.getName(), NK_TEXT_ALIGN_CENTERED);
						} catch (IllegalArgumentException e) {
						}
					}
				}
				nk_tree_state_pop(ctx);
			}
			nk_tree_state_pop(ctx);
		}
	}

}
