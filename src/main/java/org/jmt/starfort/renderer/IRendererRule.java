package org.jmt.starfort.renderer;

import org.jmt.starfort.util.Coord;
import org.jmt.starfort.world.component.IComponent;

public interface IRendererRule {
	
	/**
	 * Get components renderable by this rule
	 * 
	 * @return components renderable by this rule
	 */
	public Class<? extends IComponent>[] getRenderableComponents();
	
	/**
	 * Time to load images etc.
	 * @param nvgCtx
	 */
	public void init(long nvgCtx, Renderer r);
	
	/**
	 * Time to draw; assume this is called between nvgBeginFrame operations;
	 * @param nvgCtx
	 * @param comp
	 * @param compLoc
	 */
	public void draw(long nvgCtx, Renderer r, Coord offset, IComponent comp, Coord compLoc);

}
