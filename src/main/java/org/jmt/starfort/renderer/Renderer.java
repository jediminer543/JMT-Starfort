
package org.jmt.starfort.renderer;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.World;
import org.jmt.starfort.world.block.Block;
import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.material.IMaterial;
import org.jmt.starfort.world.material.MaterialRegistry;
import org.joml.Vector2f;

public class Renderer {
	
	//
	//
	//
	float zoom = 0.25f;
	// 1 world space = 16 render spaces
	int renderSpacePerWorldSpace = 16;
	
	Map<Class<? extends IComponent>, IRendererRule> renderSet = new HashMap<>();
	
	public HashMap<Integer, Colour> materialRenderReg = new HashMap<>();
	
	
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
	 * Initialize the renderer
	 * @param nvgCtx
	 * @param renderRules
	 */
	public void init(ArrayList<IRendererRule> renderRules) {
		for (IRendererRule rr : renderRules) {
			for (Class<? extends IComponent> comp : rr.getRenderableComponents())
				renderSet.put(comp, rr);
				System.out.println("Renderer added component");
			rr.init(this);
		}
		System.out.println("Renderer loaded");
	}
	
	/**
	 * Draws a world
	 * 
	 * TODO: Optimise; Remove Off Screen Rendering
	 * 
	 * @param w World to draw
	 * @param offset Offset to draw to
	 */
	public void draw(World w, Coord offset) {
		//glRotatef(90, 0, 0, 1);
		long startTime = System.nanoTime();
		int[] bounds = w.getBounds(true);
		for (int x = bounds[0]-1; x < bounds[3]+1; x++) {
			for (int z = bounds[2]-1; z < bounds[5]+1; z++) {
				int y = offset.y;
				Coord curLoc = new Coord(x, y, z);
				Block b = w.getBlockNoAdd(curLoc);
				if (b == null) {
					continue;
				}
				for (IComponent comp : b.getComponents()) {
					IRendererRule rr = renderSet.get(comp.getClass());
					rr.draw(this, offset, comp, curLoc);
					
				}
				
			}
		}
		long endTime = System.nanoTime();
		long frameTime = endTime - startTime;
		float FPS = (1000000000/frameTime);
		System.out.println("FPS: " + FPS);
		//glRotatef(-90, 0, 0, 1);
	}
	
}
