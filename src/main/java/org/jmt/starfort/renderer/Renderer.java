
package org.jmt.starfort.renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.jmt.starfort.renderer.JMTGl.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jmt.starfort.event.EventBus;
import org.jmt.starfort.event.EventBus.EventCallback;
import org.jmt.starfort.event.ui.EventMouseButton;
import org.jmt.starfort.event.world.EventWorldClick;
import org.jmt.starfort.logging.Logger;
import org.jmt.starfort.event.IEvent;
import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.Block;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.material.IMaterial;
import org.jmt.starfort.world.material.MaterialRegistry;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class Renderer {
	
	//
	//
	//
	float zoom = 0.25f;
	// 1 world space = 16 render spaces
	int renderSpacePerWorldSpace = 16;
	
	public float FPS = 0;
	
	/**
	 * Current render depth; placed here for custom shaders
	 */
	public int renderDepth = 0;
	
	/**
	 * Depth colour for custom shaders
	 */
	public final float[] depthCol = new float[] {0.1f, 0.1f, 0.2f, 1f};
	
	Map<Class<? extends IComponent>, IRendererRule> renderSet = new HashMap<>();
	
	public HashMap<Integer, Colour> materialRenderReg = new HashMap<>();
	
	public HashMap<World, Coord> worldOffsetMap = new HashMap<>();
	
	/**
	 * The last world rendered; will be null no world has been rendered.
	 * If accesed during rendering will be the current world being rendered.
	 */
	public World lastRenderedWorld;
	
	{
		EventBus.registerEventCallback(new EventCallback() {
			
			@Override
			public void handleEvent(IEvent ev) {
				if (!ev.getEventConsumed()) {
					EventMouseButton emb = (EventMouseButton) ev;
					if (emb.getEventAction() == GLFW.GLFW_RELEASE) {
						double[] xa = new double[1], ya = new double[1];
						GLFW.glfwGetCursorPos(emb.getEventWindow(), xa, ya);
						Coord offset = worldOffsetMap.get(lastRenderedWorld);
						Coord point = new Coord((int)Math.floor(rtwLen((float)xa[0]))-offset.x, offset.y, (int)Math.floor(rtwLen((float)ya[0]))-offset.z);
						//System.out.println("Click at X:" + xa[0] + ", Y:" + ya[0] + " " + (point).toString());
						IEvent oev = new EventWorldClick(emb.getEventWindow(), lastRenderedWorld, point);
						EventBus.fireEvent(oev);
						if (oev.getEventConsumed()) {
							ev.consumeEvent();
						}
					}
				}
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public Class<? extends IEvent>[] getProcessableEvents() {
				return new Class[] {EventMouseButton.class};
			}

			@Override
			public int getPriority() {
				return 90;
			}
		});
	}
	
	/**
	 * Returns a render coord space of the world coord; only do on a 
	 * @param worldC
	 * @param offset
	 * @return
	 */
	public Vector2f wtrCoord(Coord worldC, Coord offset) {
		Coord tmp = worldC.add(offset);
		return new Vector2f(tmp.x * 1/zoom * renderSpacePerWorldSpace, tmp.z * 1/zoom * renderSpacePerWorldSpace);
	}
	
	/**
	 * Returns a render coord space of the world coord; only do on a 
	 * @param worldC
	 * @param offset
	 * @return
	 */
	public float wtrLen(float frac) {
		return (frac) * 1/zoom * renderSpacePerWorldSpace ;
	}
	
	/**
	 * Returns a world coord space of the render coord; only do on a 
	 * @param worldC
	 * @param offset
	 * @return
	 */
	public float rtwLen(float frac) {
		return (frac) / (1/zoom * renderSpacePerWorldSpace);
	}
	
	/**
	 * Register a material's render colour
	 * 
	 * @param mat Material to map
	 * @param color Colour to map to material
	 */
	public void registerMaterial(IMaterial mat, Colour color) {
		materialRenderReg.put(MaterialRegistry.getMaterialID(mat), color);
	}
	
	/**
	 * Register a material's render colour
	 * 
	 * @param mat Material to map
	 * @return Colour mapped to material
	 */
	public Colour getMaterialColor(IMaterial mat) {
		return materialRenderReg.get(MaterialRegistry.getMaterialID(mat));
	}
	
	/**
	 * The component shader
	 */
	public int program;
	
	/**
	 * Map of render settings, doesn't need to be syncd 
	 * as rendering is all done in the main thread
	 */
	Map<String, String> settings = new HashMap<>();
	
	/**
	 * Sets a config option for this renderer
	 * 
	 * @param key Key to find
	 * @param val Value of the setting; should be serialised if necesary
	 */
	public void setRenderSetting(String key, String val) {
		if (val != null) {
			settings.put(key, val);
		} else {
			// Don't store null keys
			settings.remove(key);
		}
	}
	
	/**
	 * Gets renderer setting for a given key
	 * @param key The setting to look up
	 * @return Serialised value of the key, or null if the option doesn't exist
	 */
	public String getRenderSetting(String key) {
		return settings.get(key);
	}
	
	/**
	 * Initialize the renderer
	 * @param nvgCtx
	 * @param renderRules
	 */
	public void init(ArrayList<IRendererRule> renderRules) {
		try {
			program = jglLoadShader("".getClass().getResourceAsStream("/org/jmt/starfort/shader/ComponentShader.GLSL13.vert"), 
					"".getClass().getResourceAsStream("/org/jmt/starfort/shader/ComponentShader.GLSL13.frag"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		jglUseProgram(program);
		for (IRendererRule rr : renderRules) {
			for (Class<? extends IComponent> comp : rr.getRenderableComponents())
				renderSet.put(comp, rr);
				Logger.trace("Renderer added component", "Renderer");
			rr.init(this);
		}
		Logger.trace("Renderer loaded", "Renderer");
		jglUseProgram(0);
	}
	
	static class RRC implements Comparator<RenderPair> {

		public static final RRC INSTANCE = new RRC();
		
		@Override
		public int compare(RenderPair o1, RenderPair o2) {
			return o1.rr.getPriority() - o2.rr.getPriority();
		}

		
	}
	
	static class RenderPair  {
		IRendererRule rr;
		IComponent comp;
		
		public RenderPair(IRendererRule rr, IComponent comp) {
			this.rr = rr;
			this.comp = comp;
		}
	}
	
	Map<Class<? extends IComponent>, IRendererRule> currentRenderSet = new HashMap<>();
	
	/**
	 * Draws a world
	 * 
	 * TODO: Optimise; Remove Off Screen Rendering; Multi-layer Rendering
	 * 
	 * @param w World to draw
	 * @param offset Offset to draw to
	 */
	public void draw(World w, Coord offset) {
		worldOffsetMap.put(w, offset);
		lastRenderedWorld = w;
		currentRenderSet.clear();
		for (Entry<Class<? extends IComponent>, IRendererRule> e : renderSet.entrySet()) {
			if (!e.getValue().disabled(this)) {
				currentRenderSet.put(e.getKey(), e.getValue());
			}
		}
		//glRotatef(90, 0, 0, 1);
		long startTime = System.nanoTime();
		int[] bounds = w.getBounds(true);
		glPushMatrix();
		jglPushMatrix();
		drawLayer(w, offset, bounds, 3);
		drawLayer(w, offset, bounds, 2);
		drawLayer(w, offset, bounds, 1);
		drawLayer(w, offset, bounds, 0);
		glPopMatrix();
		jglPopMatrix();
		long endTime = System.nanoTime();
		long frameTime = endTime - startTime;
		FPS = (1000000000/frameTime);
		//System.out.println("FPS: " + FPS);
		//glRotatef(-90, 0, 0, 1);
	}
	
	/**
	 * Allows for render rules to reset the shader mode after updating it
	 */
	public void resetShaderMode() {
		glUniform1i(jglGetUniformLocation("u_flags"), 1<<0 | 1<<1);
		glUniform1i(jglGetUniformLocation("u_tex"), 0); // Standardise texture setup
		glUniform1i(jglGetUniformLocation("u_mask"), 1); // Standardise mask setup
	}
	
	private void drawLayer(World w, Coord offset, int[] bounds, int depth) {
		renderDepth = depth;
		jglUseProgram(program);
		glUniform1i(jglGetUniformLocation("u_depth"), depth);
		glUniform4fv(jglGetUniformLocation("u_depthCol"), new float[] {0.1f, 0.1f, 0.2f, 1f});
		resetShaderMode();
		jglUseProgram(0);
		glPushMatrix();
		glTranslatef(0, 0, -wtrLen(depth));
		jglPushMatrix();
		jglTranslatef(0, 0, -wtrLen(depth));
		for (int x = bounds[0]-1; x < bounds[3]+1; x++) {
			for (int z = bounds[2]-1; z < bounds[5]+1; z++) {
				int y = offset.y - depth;
				Coord curLoc = new Coord(x, y, z);
				Block b = w.getBlockNoAdd(curLoc);
				if (b == null) {
					continue;
				}
				List<RenderPair> rra = new ArrayList<>();
				try {
				for (IComponent comp : b.getComponents()) {
					IRendererRule rr = currentRenderSet.get(comp.getClass());
					if (rr != null)
						rra.add(new RenderPair(rr, comp));
					
				}
				} catch (ConcurrentModificationException cme) {
					//System.err.println("Rendering concurrent modification exception - Not a problem - Skipping tile - WARN");
					//cme.printStackTrace();
				} catch (NullPointerException npe) {
					System.err.println("Rendering NPE - POSSIBLY a problem - Skipping tile - WARN");
					npe.printStackTrace();
				}
				Collections.sort(rra, RRC.INSTANCE);
				for (RenderPair rp : rra) {
					try {
						rp.rr.draw(this, offset, rp.comp, curLoc);
					} catch (Exception e) {
						Logger.error("Exception in render rule; ignoring and hoping it goes away", "Renderer");
						Logger.error("Exception: " + e.toString(), "Renderer");
					}
				}
			}
		}
		glPopMatrix();
		jglPopMatrix();
	}
	
}