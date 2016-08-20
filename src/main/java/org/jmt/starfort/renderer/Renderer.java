package org.jmt.starfort.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.block.Block;
import org.jmt.starfort.world.component.IComponent;
import org.joml.Vector2f;

public class Renderer {
	
	/**
	 * Sf = 1/zoom
	 */
	float zoom = 1f;
	
	// 1 world space = 16 render spaces
	int renderSpacePerWorldSpace = 16;
	
	Map<Class<? extends IComponent>, IRendererRule> components = new HashMap<>();
	
	ArrayList<Float> glBuffer = new ArrayList<Float>();
	
	/**
	 * Returns a render coord space of the world coord; only do on a 
	 * @param worldC
	 * @param offset
	 * @return
	 */
	public Vector2f worldToRenderSpatialConvert(Coord worldC, Coord offset) {
		Coord tmp = worldC.addR(offset);
		return new Vector2f(tmp.x * 1/zoom * renderSpacePerWorldSpace, tmp.z * 1/zoom * renderSpacePerWorldSpace);
	}
	
	/**
	 * Returns a render coord space of the world coord; only do on a 
	 * @param worldC
	 * @param offset
	 * @return
	 */
	public float worldToRenderLengthConvert(float frac) {
		return (frac) * 1/zoom * renderSpacePerWorldSpace ;
	}
	
	public void Init(long nvgCtx, ArrayList<IRendererRule> renderRules) {
		for (IRendererRule rr : renderRules) {
			for (Class<? extends IComponent> comp : rr.getRenderableComponents())
				components.put(comp, rr);
				System.out.println("Renderer added component");
			rr.init(nvgCtx);
		}
		System.out.println("Renderer loaded");
	}
	
	public void Draw(long nvgCtx, World w, Coord offset) {
		long startTime = System.nanoTime();
		int[] bounds = w.getBounds(true);
		for (int x = bounds[0]-1; x < bounds[3]+1; x++) {
			for (int z = bounds[2]-1; z < bounds[5]+1; z++) {
				int y = offset.y;
				Coord curLoc = new Coord(x, y, z);
				Block b = w.getBlockNoAdd(curLoc);
				if (b == null) 
					continue;
				for (Entry<Class<? extends IComponent>, IRendererRule> e : components.entrySet()) {
					for (IComponent comp : b.getCompInstances(e.getKey())) {
						e.getValue().Draw(nvgCtx, this, offset, comp, curLoc);
					}
				}
				
			}
		}
		long endTime = System.nanoTime();
		long frameTime = endTime - startTime;
		float FPS = (1000000000/frameTime);
		System.out.println("FPS: " + FPS);
	}
	
}
